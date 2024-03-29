import * as React from 'react';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import StringUtil from "../utils/StringUtil";

export default function WhoisCard() {
    return (
        <Card sx={{ width: '60%', marginTop: '2%', marginLeft: 'auto', marginRight: 'auto'}}>
            <CardMedia
                sx={{ height: 140 }}
                image="/images/whois.png"
                title="whois"
            />
            <CardContent>
                <Typography gutterBottom variant="h5" component="div">
                    Whois
                </Typography>
                <Typography variant="body2" color="text.secondary">{StringUtil.whoisInfo}</Typography>
            </CardContent>
            <CardActions>
                <Button target='_blank' href="https://lookup.icann.org/en" size="small">Learn More</Button>
            </CardActions>
        </Card>
    );
}
