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
                    Opensquare Service Platforms provide a way for you to search popular social
                    media sites and index large quantities of its public data. OpenSquare will
                    never expose private and protected data of any user or target.
                    <br></br>
                    <br></br>
                    Opensquare's YouTube Platform allows agents to search within our databases for
                    video's, providing detailed information about the channel and it's content.
                    Searched video results provide a full detailed log and transcripts. You have
                    the option to search videos based on terms, and the most relevant video's will
                    be provided. This is powered by a strong vector database that creates text
                    embeddings on each video's transcript using OpenAI's Text Embedding Ada-002 Large
                    Language Model. You can search YouTube video's providing a video identification
                    code. If video's aren't present in our database, they'll be added for the community.
                    Feel free to provide any insights or feedback to our team at opensentop@gmail.com.
                </Typography>
            </CardContent>
            <CardActions>
                <Button target='_blank' href="https://www.youtube.com/" size="small">Documentation</Button>
            </CardActions>
        </Card>
    );
}
