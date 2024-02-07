import * as React from 'react';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';

export default function YouTubeCard() {
    return (
        <Card sx={{ width: '60%', marginTop: '2%', marginLeft: 'auto', marginRight: 'auto'}}>
            <CardMedia
                sx={{ height: 140}}
                image="/images/youtube.png"
                title="YouTube Platform"
            />
            <CardContent>
                <Typography gutterBottom variant="h5" component="div">
                    OpenSquare YouTube Platform
                </Typography>
                <Typography variant="body2" color="text.secondary">
                    OpenSquare Service Platforms provide a way for you to search popular social
                    media sites and index large quantities of its public data. OpenSquare will
                    never expose private and protected data of any user or target.
                </Typography>
            </CardContent>
            <CardActions>
                <Button target='_blank' href="https://www.youtube.com/" size="small">Documentation</Button>
            </CardActions>
        </Card>
    );
}
