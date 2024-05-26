import * as React from "react";
import Grid from "@mui/material/Grid";
import Paper from "@mui/material/Paper";
import InputBase from '@mui/material/InputBase';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import SearchIcon from '@mui/icons-material/Search';
import { styled } from '@mui/material/styles';
import axios from "axios";
import {useEffect, useState} from "react";
import Box from "@mui/material/Box";
import GenerateCard from "./GenerateCard";
import { quantum } from "ldrs";

export default function Generate() {
    const [promptImageResponse, setPromptImageResponse] = React.useState('');
    const [showCradle, setShowCradle] = useState(false);
    quantum.register();

    const Item = styled(Paper)(({theme}) => ({
        backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#d8ecf3',
        ...theme.typography.body2,
        padding: theme.spacing(5),
        color: theme.palette.text.secondary,
        textAlign: 'left',
        maxWidth: '100%',
        palette: {
            background: {
                tertiary: '#d8ecf3'
            },
            text: {
                primary: '#fff'
            }
        }
    }));

    function getAxiosConfiguration() {
        return {
            timeout: 60000,
            signal: AbortSignal.timeout(60000)
        };
    }

    useEffect(() => {
        console.log(promptImageResponse);
    }, [promptImageResponse]);

    async function handlePrompt(query) {
        try {
            setShowCradle(true);
            const response = await axios.get(`/opensquare/api/imageTracking/generate_vision_pulse_from_prompt?q=${query}`, getAxiosConfiguration());
            if (response.data !== null) {
                console.log(response.data);
                setShowCradle(false);
                setPromptImageResponse(response.data)
                return response.data;
            }
        } catch (error) {
            console.log(error);
            setShowCradle(false);
        }
    }

    async function handleImagePrompt(e) {
        e.preventDefault();
        let queryElement = document.getElementById('imagePrompt');
        let query = queryElement.value.trim();
        if (query !== null && query.length > 0) {
            queryElement.value = '';
            await handlePrompt(query);
        }
    }

    return (
        <React.Fragment>
            <Grid item xs={12} justifyContent='center' alignItems='center'>
                <GenerateCard/>
                <Paper
                    component="form"
                    sx={{
                        p: '2px 4px',
                        display: 'flex',
                        width: '95%',
                        marginLeft: '2.5%',
                        marginTop: '5%',
                        backgroundColor: 'tertiary'
                    }}
                >
                    <InputBase
                        fullWidth={true}
                        sx={{ml: 1, flex: 1}}
                        placeholder="Enter Prompt"
                        id={'imagePrompt'}
                        inputProps={{'aria-label': 'prompt'}}
                    />
                    <IconButton onClick={e => handleImagePrompt(e)} type="submit" sx={{p: '10px'}} aria-label="prompt">
                        <SearchIcon/>
                    </IconButton>
                    <Divider sx={{height: 28, m: 0.5}} orientation="vertical"/>
                </Paper>
                <div>
                    { showCradle && <l-quantum
                        size="45"
                        speed="1.75"
                        color="black">
                    </l-quantum>}
                    {promptImageResponse !== '' && <Box sx={{flexGrow: 1, overflow: 'hidden', px: 3}}>
                        <img src={promptImageResponse} alt="Generated" style={{maxWidth: '50%', height: 'auto'}}/>
                    </Box>}
                </div>
            </Grid>
        </React.Fragment>
    );
}