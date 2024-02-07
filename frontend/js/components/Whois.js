import * as React from "react";
import Typography from "@mui/material/Typography";
import Grid from "@mui/material/Grid";
import Paper from "@mui/material/Paper";
import InputBase from '@mui/material/InputBase';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import SearchIcon from '@mui/icons-material/Search';
import OpenInBrowserIcon from '@mui/icons-material/OpenInBrowser';
import Box from '@mui/material/Box';
import Stack from '@mui/material/Stack';
import { styled } from '@mui/material/styles';
import axios from "axios";
import {useEffect} from "react";
import WhoisCard from "./WhoisCard";

export default function Whois() {
    const [whois, setWhois] = React.useState({});

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
            timeout: 6000,
            signal: AbortSignal.timeout(6000)
        };
    }

    useEffect(() => {
        console.log(whois);
    }, [whois]);

    async function handleWhoisDomainSearch(query) {
        await axios.get(`/opensquare/api/footprints/whois?domain=${query}`, getAxiosConfiguration())
            .then((response) => {
                setWhois(response.data)
            })
            .catch((error) => {
                console.log(error)
            })
    }

    async function handleSearch(e){
        e.preventDefault();
        const query = document.getElementById('whoisSearch').value;
        document.getElementById('whoisSearch').value = '';
        await handleWhoisDomainSearch(query);
    }

    function handleBrowserOpener(e) {
        e.preventDefault();
        window.open('https://google.com', '_blank');
    }

    function getDomainStatus(domainStatus) {
        return domainStatus.split(' ')[0].trim();
    }

    function getDomainStatusUrl(domainStatus) {
        let url = domainStatus.split(' ')[1];
        return url.substring(1, url.length - 1);
    }
    return (
        <React.Fragment>
            <Grid item xs={12} justifyContent='center' alignItems='center'>
                <WhoisCard />
                <Paper
                    component="form"
                    sx={{ p: '2px 4px', display: 'flex', width: '95%', marginLeft: '2.5%', marginTop: '5%', backgroundColor: 'tertiary' }}
                >
                    <InputBase
                        fullWidth={true}
                        sx={{ ml: 1, flex: 1 }}
                        placeholder="Enter Domain address"
                        id={'whoisSearch'}
                        inputProps={{ 'aria-label': 'whois lookup search' }}
                    />
                    <IconButton onClick={handleSearch} type="button" sx={{ p: '10px' }} aria-label="search">
                        <SearchIcon />
                    </IconButton>
                    <Divider sx={{ height: 28, m: 0.5 }} orientation="vertical" />
                    <IconButton onClick={handleBrowserOpener} color="primary" sx={{ p: '10px' }} aria-label="directions">
                        <OpenInBrowserIcon />
                    </IconButton>
                </Paper>
                <Box sx={{ flexGrow: 1, overflow: 'hidden', px: 3 }}>
                    {whois['domainName'] !== undefined && <Item
                        sx={{
                            my: 1,
                            mx: 'auto',
                            p: 2,
                        }}
                    >
                        <Stack spacing={2} direction="row">
                            <Typography sx={{ font: 'primary', fontSize: '20px' }} noWrap><b>Domain Information</b></Typography>
                        </Stack>
                        <Stack spacing={1} direction="column" marginTop="3%">
                                <Typography noWrap><b>Name: </b> {whois['domainName']}</Typography>
                                <Typography noWrap><b>Registry Domain ID: </b> {whois['registryDomainId']}</Typography>
                                <Typography noWrap><b>Domain Status: </b><a target='_blank' href={getDomainStatusUrl(whois['domainStatus'])}>{getDomainStatus(whois['domainStatus'])}</a></Typography>
                                <Typography noWrap><b>NameServers: </b></Typography>
                                <Typography noWrap>{whois['nameServer']}</Typography>
                                <Typography noWrap><b>Created: </b>{whois['creationDate']}</Typography>
                                <Typography noWrap><b>Updated: </b>{whois['updatedDate']}</Typography>
                        </Stack>
                    </Item> }
                    {whois['adminCity'] !== undefined && <Item
                        sx={{
                            my: 1,
                            mx: 'auto',
                            p: 2,
                        }}
                    >
                        <Stack spacing={2} direction="row">
                            <Typography sx={{ font: 'primary', fontSize: '20px' }}  noWrap><b>Contact Information</b></Typography>
                        </Stack>
                        <Stack spacing={1} direction="column" marginTop="3%">
                                <Typography><b>City: </b> {whois['adminCity']}</Typography>
                                <Typography><b>Country: </b> {whois['adminCountry']}</Typography>
                                <Typography><b>Email: </b> {whois['adminEmail']}</Typography>
                                <Typography><b>Fax: </b> {whois['adminFax']}</Typography>
                                <Typography><b>Name: </b>{whois['adminName']}</Typography>
                                <Typography><b>Organization: </b>{whois['adminOrganization']}</Typography>
                                <Typography><b>Phone: </b>{whois['adminPhone']}</Typography>
                                <Typography><b>Postal Code: </b> {whois['adminPostalCode']}</Typography>
                                <Typography><b>Street: </b> {whois['adminStreet']}</Typography>
                        </Stack>
                    </Item> }
                    { whois['registrar'] !== undefined && <Item
                        sx={{
                            my: 1,
                            mx: 'auto',
                            p: 2,
                        }}
                    >
                        <Stack spacing={2} direction="row">
                            <Typography sx={{ font: 'primary', fontSize: '20px' }}  noWrap><b>Registrar Information</b></Typography>
                        </Stack>
                        <Stack spacing={1} direction="column" marginTop="3%">
                                <Typography><b>Registrar Name: </b> {whois['registrar']}</Typography>
                                <Typography><b>Abuse contact email: </b> {whois['registrarAbuseContactEmail']}</Typography>
                                <Typography><b>Abuse contact phone: </b>{whois['registrarAbuseContactPhone']}</Typography>
                                <Typography><b>IANA ID: </b>{whois['registrarIanaId']}</Typography>
                                <Typography><b>Registration expiration date: </b>{whois['registrarRegistrationExpirationDate']}</Typography>
                                <Typography><b>Registrar URL: </b> <a target='_blank' href={whois['registrarUrl']}>{whois['registrarUrl']}</a></Typography>
                                <Typography><b>Registrar Whois Server: </b> {whois['registrarWhoisServer']}</Typography>
                        </Stack>
                    </Item> }
                    { whois['dnssec'] !== undefined && <Item
                        sx={{
                            my: 1,
                            mx: 'auto',
                            p: 2,
                        }}
                    >
                        <Stack spacing={2} direction="row">
                            <Typography sx={{ font: 'primary', fontSize: '20px' }}  noWrap><b>DNSSEC Information</b></Typography>
                        </Stack>
                        <Stack spacing={1} direction="column" marginTop="3%">

                                <Typography><b>Delegation Signed: </b> {whois['dnssec']}</Typography>
                        </Stack>
                    </Item> }
                </Box>
            </Grid>
        </React.Fragment>
    );
}
