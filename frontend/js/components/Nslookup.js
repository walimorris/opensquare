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
import NslookupCard from "./NslookupCard";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Stack from "@mui/material/Stack";

export default function Nslookup() {
    const [nslookup, setNslookup] = React.useState([]);

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
        console.log(nslookup);
    }, [nslookup]);

    async function handleNslookupSearch(query) {
        await axios.get(`/opensquare/api/footprints/nslookup?domain=${query}`, getAxiosConfiguration())
            .then((response) => {
                setNslookup(response.data)
            })
            .catch((error) => {
                console.log(error)
            })
    }

    async function handleSearch(e){
        e.preventDefault();
        let queryElement = document.getElementById('nslookupSearch');
        let query = queryElement.value.trim();
        if (query !== null && query.length > 0) {
            queryElement.value = '';
            await handleNslookupSearch(query);

        }
    }

    return (
        <React.Fragment>
            <Grid item xs={12} justifyContent='center' alignItems='center'>
                <NslookupCard />
                <Paper
                    component="form"
                    sx={{ p: '2px 4px', display: 'flex', width: '95%', marginLeft: '2.5%', marginTop: '5%', backgroundColor: 'tertiary' }}
                >
                    <InputBase
                        fullWidth={true}
                        sx={{ ml: 1, flex: 1 }}
                        placeholder="Enter Domain"
                        id={'nslookupSearch'}
                        inputProps={{ 'aria-label': 'nslookup lookup search' }}
                    />
                    <IconButton onClick={e => handleSearch(e)} type="submit" sx={{ p: '10px' }} aria-label="search">
                        <SearchIcon />
                    </IconButton>
                    <Divider sx={{ height: 28, m: 0.5 }} orientation="vertical" />
                </Paper>
                { nslookup.nsLookupFootPrintList !== undefined && <Box sx={{ flexGrow: 1, overflow: 'hidden', px: 3 }}>
                    { nslookup.nsLookupFootPrintList.map(nslookupItem => (
                    <Item
                        sx={{
                            my: 1,
                            mx: 'auto',
                            p: 2,
                        }}
                    >
                        <Stack spacing={1} direction="column" marginTop="3%"><Typography><b>Name: </b>{nslookupItem.name}</Typography>
                            <Typography><b>Address: </b>{nslookupItem.address}</Typography>
                        </Stack>
                    </Item> ))}
                </Box> }
            </Grid>
        </React.Fragment>
    );
}
