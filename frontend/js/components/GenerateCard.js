import * as React from 'react';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import StringUtil from "../utils/StringUtil";

export default function GenerateCard() {
    return (
        <Card sx={{ width: '60%', marginTop: '2%', marginLeft: 'auto', marginRight: 'auto'}}>
            <CardMedia
                sx={{ height: 235 }}
                image="/images/osint-image.png"
                title="ai-generate"
            />
            <CardContent>
                <Typography gutterBottom variant="h5" component="div">
                    OSINT with Generative AI
                </Typography>
                <Typography variant="body2" color="text.secondary">{StringUtil.generate_with_ai}<b>opensentop@gmail.com</b>.
                </Typography>
            </CardContent>
            <CardActions>
                <Button target='_blank' href="https://openai.com/" size="small">Learn More</Button>
            </CardActions>
        </Card>
    );
}