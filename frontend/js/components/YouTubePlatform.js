import {useEffect, useState} from 'react';
import axios from "axios";
import '../../css/platform-options.css';
import {styled} from "@mui/material/styles";
import Paper from "@mui/material/Paper";
import Grid from "@mui/material/Grid";
import YouTubeCard from './YouTubeCard';
import InputBase from "@mui/material/InputBase";
import IconButton from "@mui/material/IconButton";
import SearchIcon from "@mui/icons-material/Search";
import Divider from "@mui/material/Divider";
import Box from "@mui/material/Box";
import Stack from "@mui/material/Stack";
import Typography from "@mui/material/Typography";
import LinearProgress, { linearProgressClasses } from '@mui/material/LinearProgress';
import PropTypes from 'prop-types';
import YouTubeVideoCard from "./YouTubeVideoCard";

const React = require('react');

const YouTubePlatform = ({isSelected}) => {
    const kafkaTask = "video-search";
    const [videoId, setVideoId] = useState('');
    const [updatedVideoId, setUpdatedVideoId] = useState(videoId);
    const [kafkaProgress, setKafkaProgress] = useState('STARTING');
    const [kafkaPercentage, setKafkaPercentage] = useState(0);
    const [inProgress, setInProgress] = useState(false);

    // search
    const [vectorSearchQuery, setVectorSearchQuery] = useState('');
    const [updatedVectorSearchQuery, setUpdatedVectorSearchQuery] = useState(vectorSearchQuery);
    const [vectorYouTubeSearchResults, setVectorYouTubeSearchResults] = useState([]);

    const UNDER_SCORE = '_';
    const EMPTY_SPACE = ' ';

    const Item = styled(Paper)(({ theme }) => ({
        backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#d8ecf3',
        ...theme.typography.body2,
        padding: theme.spacing(5),
        color: theme.palette.text.secondary,
        textAlign: 'left',
        maxWidth: '100%',
        palette:{
            background: {
                tertiary: '#d8ecf3'
            },
            text: {
                primary: '#fff'
            }
        }
    }));

    const BorderLinearProgress = styled(LinearProgress)(({ theme }) => ({
        height: 10,
        borderRadius: 5,
        [`&.${linearProgressClasses.colorPrimary}`]: {
            backgroundColor: theme.palette.grey[theme.palette.mode === 'light' ? 200 : 800],
        },
        [`& .${linearProgressClasses.bar}`]: {
            borderRadius: 5,
            backgroundColor: theme.palette.mode === 'light' ? '#1a90ff' : '#308fe8',
        },
    }));

    async function onVectorYouTubeSearchSubmit() {
        setVectorSearchQuery(vectorSearchQuery);
        document.getElementById('youtubeSearch').value = '';
        const vectorSearchResults = await handleVectorYouTubeSearch();
    }


    async function onInputSubmit() {
        setUpdatedVideoId(videoId);
        document.getElementById('youtubeSearch').value = '';
        const youtubeSearchResponse = await handleOpenSearchForYouTubeVideoId();
        setVideoId('');
        const youtubePlatformObject = youtubeSearchResponse.data;
        let locationUrl = youtubeSearchResponse.headers.get("Location");
        console.log(locationUrl);

        if (youtubePlatformObject !== undefined) {
            console.log('processing request');
            console.log(`update at url: ${locationUrl}`);

            // call location endpoint for kafka progress
            if (locationUrl) {
                setInProgress(true);

                locationUrl = locationUrl.replace("http://localhost:8081", "/opensentop/api");
                let kafkaProgressResponse = await handleKafkaPoll(locationUrl);
                let taskPercentage = kafkaProgressResponse.data['percentageComplete'];
                let taskStatus = kafkaProgressResponse.data['status'];
                setKafkaPercentage(taskPercentage);
                setKafkaProgress(taskStatus);

                let intervalId = setInterval(async () => {
                    kafkaProgressResponse = await handleKafkaPoll(locationUrl);
                    taskPercentage = kafkaProgressResponse.data['percentageComplete'];
                    taskStatus = kafkaProgressResponse.data['status'];

                if (taskStatus !== undefined && taskStatus.length > 0) {
                    setKafkaPercentage(taskPercentage + 0.1);
                    setKafkaProgress(taskStatus);
                }

                if (taskStatus === 'FINISHED') {
                    clearInterval(intervalId);
                }

                }, 10000);
            }
        }
    }

    /**
     * Formats the Kafka progress message to a nicely readable representation.
     *
     * @param message the message from kafka producer
     *
     * @returns {string} slightly more appealing message
     */
    function formatKafkaMessage(message) {
        if (message.includes(UNDER_SCORE)) {
            message = message.replaceAll(UNDER_SCORE, EMPTY_SPACE);
        }
        return message.substring(0, 1).toUpperCase()
            .concat(message.substring(1, message.length).toLowerCase());
    }

    function onChange(e) {
        setVectorSearchQuery(e.target.value);
    }

    function getAxiosConfiguration() {
        return {
            timeout: 3000,
            signal: AbortSignal.timeout(6000)
        };
    }

    useEffect(() => {
        console.log(vectorYouTubeSearchResults.data);
    }, [vectorYouTubeSearchResults.data]);

    async function handleVectorYouTubeSearch() {
        return new Promise((resolve) => {
            setTimeout(() => {
                resolve(
                    axios.get(`/opensquare/api/youtube/en/transcripts/search?q=${vectorSearchQuery}`, getAxiosConfiguration())
                        .then(response => {
                            console.log(response.data);
                            setVectorYouTubeSearchResults(response.data);
                            if (response.data !== null) {
                                return response;
                            }
                            console.log(`passed data: ${vectorSearchQuery}`);
                        })
                        .catch(error => {
                            console.log(error);
                        })
                );
            }, 1000);
        });
    }

    async function handleKafkaPoll(location) {
        return new Promise(resolve => {
            setTimeout( () => {
                resolve(
                    axios.get(location, getAxiosConfiguration())
                        .then(response => {
                            console.log(response.data);
                            if (response.data !== null) {
                                return response;
                            }
                            console.log(`location: ${location}`);
                        })
                        .catch(error => {
                            console.log(error);
                        })
                );
            }, 1000);
        });
    }

    async function handleOpenSearchForYouTubeVideoId() {
        return new Promise((resolve) => {
            setTimeout(() => {
                resolve(
                    axios.get(`/opensentop/api/youtube/video_search?videoId=${videoId}&name=${kafkaTask}`, getAxiosConfiguration())
                        .then(response => {
                            console.log(response.data);
                            if (response.data !== null) {
                                return response;
                            }
                            console.log(`passed data: ${videoId}`);
                        })
                        .catch(error => {
                            console.log(error);
                        })
                );
            }, 1000);
        });
    }

    function LinearProgressWithLabel(props) {
        return (
            <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <Box sx={{ width: '100%', mr: 1 }}>
                    <BorderLinearProgress variant="determinate" value={props.value}/>
                </Box>
                <Box sx={{ minWidth: 35 }}>
                    <Typography variant="body2" color="text.secondary">{`${Math.round(
                        props.value,
                    )}%`}</Typography>
                </Box>
            </Box>
        );
    }

    LinearProgressWithLabel.propTypes = {
        /**
         * The value of the progress indicator for the determinate and buffer variants.
         * Value between 0 and 100.
         */
        value: PropTypes.number.isRequired,
    };

    return (
        <React.Fragment>
            <Grid item xs={12} justifyContent='center' alignItems='center'>
                <YouTubeCard />
                <Paper
                    component="form"
                    sx={{ p: '2px 4px', display: 'flex', width: '95%', marginLeft: '2.5%', marginTop: '5%', backgroundColor: 'tertiary' }}
                >
                    <InputBase
                        fullWidth={true}
                        sx={{ ml: 1, flex: 1 }}
                        placeholder="Video Search"
                        id={'youtubeSearch'}
                        onChange={onChange}
                        inputProps={{ 'aria-label': 'youtube video search' }}
                    />
                    <IconButton onClick={onVectorYouTubeSearchSubmit} type="button" sx={{ p: '10px' }} aria-label="search">
                        <SearchIcon />
                    </IconButton>
                    <Divider sx={{ height: 28, m: 0.5 }} orientation="vertical" />
                </Paper>
                { vectorYouTubeSearchResults.length > 0 && <Box sx={{ flexGrow: 1, overflow: 'hidden', px: 3 }}>
                    { vectorYouTubeSearchResults.map(video => (
                        <YouTubeVideoCard video={video} />))}
                </Box>}
            </Grid>
        </React.Fragment>
    )
};

export default YouTubePlatform;
