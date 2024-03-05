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
    const metaImage = getFirstAvailableImage();

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
        const query = document.getElementById('backlinksSearch').value;
        document.getElementById('backlinksSearch').value = '';
        await handleBacklinksSearch(query);
    }


    /**
     * todo: create list of possible image blocking parts (s3, fbsbx, etc)
     *
     * Getting the first available image is meant for the purpose of rendering a generic
     * valid image returned from the backlink results.
     *
     * Valid images contain https, are defined, and are publicly available.
     *
     * @return {*}
     */
    function getFirstAvailableImage() {
        let image;
        if (backlinks !== undefined) {
            backlinks.forEach(backlink => {
                const pagemap = backlink.pagemap;
                if (pagemap !== undefined && pagemap.metatags !== undefined && pagemap.metatags[0] !== undefined) {
                    let metatags = pagemap.metatags[0];
                    let ogImage = metatags['og:image'];
                    if (ogImage !== undefined && !ogImage.includes('s3') && !ogImage.includes('fbsbx')) {
                        if (ogImage.includes('https')) {
                            image = ogImage;
                        }
                    }
                }
            })
        }
        console.log("this is image: " + image)
        return image;
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
                    <IconButton onClick={handleSearch} type="button" sx={{ p: '10px' }} aria-label="search">
                        <SearchIcon />
                    </IconButton>
                    <Divider sx={{ height: 28, m: 0.5 }} orientation="vertical" />
                </Paper>
                { backlinks.length > 0 && <Box sx={{ flexGrow: 1, overflow: 'hidden', px: 3 }}>
                    { backlinks.map(backlink => (
                        <BackLinkCard backlink={backlink} metaImage={metaImage} />))}
                </Box>}
            </Grid>
        </React.Fragment>
    );
}
