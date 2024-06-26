import * as React from 'react';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import StringUtil from "../utils/StringUtil";

export default function YouTubeCard() {
    return (
        <Card sx={{ width: '60%', marginTop: '2%', marginLeft: 'auto', marginRight: 'auto'}}>
            <CardMedia
                sx={{ height: 235}}
                image="/images/youtube-updated.png"
                title="YouTube Platform"
            />
            <CardContent>
                <Typography gutterBottom variant="h5" component="div">
                    OpenSquare YouTube Platform
                </Typography>
                <Typography
                    variant="body2"
                    color="text.secondary">
                    {StringUtil.youtubeInfo_1}
                    <br></br><br></br>
                    {StringUtil.youtubeInfo_2}
                    <b>opensentop@gmail.com</b>.
                </Typography>
            </CardContent>
            <CardActions>
                <Button target='_blank' href="https://www.youtube.com/" size="small">Documentation</Button>
            </CardActions>
        </Card>
    );
}
