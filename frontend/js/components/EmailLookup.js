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
import EmailLookupCard from "./EmailLookupCard";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Stack from "@mui/material/Stack";

export default function EmailLookup() {
    const [emaillookup, setEmailLookup] = React.useState([]);

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
        console.log(emaillookup);
    }, [emaillookup]);

    async function handleEmailLookupSearch(query) {
        await axios.get(`/opensquare/api/verify/disposable?emailAddress=${query}`, getAxiosConfiguration())
            .then((response) => {
                setEmailLookup(response.data)
            })
            .catch((error) => {
                console.log(error)
            })
    }

    async function handleSearch(e){
        e.preventDefault();
        const query = document.getElementById('emailLookupSearch').value;
        document.getElementById('emailLookupSearch').value = '';
        await handleEmailLookupSearch(query);
    }

    return (
        <React.Fragment>
            <Grid item xs={12} justifyContent='center' alignItems='center'>
                <EmailLookupCard />
                <Paper
                    component="form"
                    sx={{ p: '2px 4px', display: 'flex', width: '95%', marginLeft: '2.5%', marginTop: '5%', backgroundColor: 'tertiary' }}
                >
                    <InputBase
                        fullWidth={true}
                        sx={{ ml: 1, flex: 1 }}
                        placeholder="Enter Email Address"
                        id={'emailLookupSearch'}
                        inputProps={{ 'aria-label': 'email lookup search' }}
                    />
                    <IconButton onClick={handleSearch} type="button" sx={{ p: '10px' }} aria-label="search">
                        <SearchIcon />
                    </IconButton>
                    <Divider sx={{ height: 28, m: 0.5 }} orientation="vertical" />
                </Paper>
                { emaillookup.domainName !== undefined && <Box sx={{ flexGrow: 1, overflow: 'hidden', px: 3 }}>
                    <Item sx={{my: 1, mx: 'auto', p: 2,}}>
                        <Stack spacing={1} direction="column" marginTop="3%"><Typography><b>Domain</b></Typography>
                            <Typography><b>Address: </b>{emaillookup.domainName}</Typography>
                        </Stack>
                    </Item>
                </Box> }
            </Grid>
        </React.Fragment>
    );
}