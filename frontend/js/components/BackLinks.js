import * as React from "react";
import Grid from "@mui/material/Grid";
import Paper from "@mui/material/Paper";
import InputBase from '@mui/material/InputBase';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import SearchIcon from '@mui/icons-material/Search';
import { styled } from '@mui/material/styles';
import axios from "axios";
import {useEffect} from "react";
import Box from "@mui/material/Box";
import BackLinkInfoCard from "./BackLinkInfoCard";
import BackLinkCard from "./BackLinkCard";

export default function BackLinks() {
    const [backlinks, setBacklinks] = React.useState([]);

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

    function getAxiosConfiguration() {
        return {
            timeout: 3000,
            signal: AbortSignal.timeout(6000)
        };
    }

    useEffect(() => {
        console.log(backlinks);
    }, [backlinks]);

    async function handleBacklinksSearch(query) {
        await axios.get(`/opensquare/api/footprints/backlinks?url=${query}`, getAxiosConfiguration())
            .then((response) => {
                setBacklinks(response.data)
            })
            .catch((error) => {
                console.log(error)
            })
    }

    async function handleSearch(e){
        e.preventDefault();
        let queryElement = document.getElementById('backlinksSearch');
        let query = queryElement.value.trim();
        if (query !== null && query.length > 0) {
            queryElement.value = '';
            await handleBacklinksSearch(query);

        }
    }

    return (
        <React.Fragment>
            <Grid item xs={12} justifyContent='center' alignItems='center'>
                <BackLinkInfoCard />
                <Paper
                    component="form"
                    sx={{ p: '2px 4px', display: 'flex', width: '95%', marginLeft: '2.5%', marginTop: '5%', backgroundColor: 'tertiary' }}
                >
                    <InputBase
                        fullWidth={true}
                        sx={{ ml: 1, flex: 1 }}
                        placeholder="Enter Domain"
                        id={'backlinksSearch'}
                        inputProps={{ 'aria-label': 'backlink search' }}
                    />
                    <IconButton onClick={e => handleSearch(e)} type="submit" sx={{ p: '10px' }} aria-label="search">
                        <SearchIcon />
                    </IconButton>
                    <Divider sx={{ height: 28, m: 0.5 }} orientation="vertical" />
                </Paper>
                { backlinks.length > 0 && <Box sx={{ flexGrow: 1, overflow: 'hidden', px: 3 }}>
                    { backlinks.map(backlink => (
                        <BackLinkCard backlink={backlink} />))}
                </Box>}
            </Grid>
        </React.Fragment>
    );
}
