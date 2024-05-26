import * as React from 'react';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import StringUtil from "../utils/StringUtil";

export default function NslookupCard() {
    return (
        <Card sx={{ width: '60%', marginTop: '2%', marginLeft: 'auto', marginRight: 'auto'}}>
            <CardMedia
                sx={{ height: 235 }}
                image="/images/nslookup-updated.png"
                title="nslookup"
            />
            <CardContent>
                <Typography gutterBottom variant="h5" component="div">
                    NSLookup
                </Typography>
                <Typography variant="body2" color="text.secondary">{StringUtil.nslookupInfo}</Typography>
            </CardContent>
            <CardActions>
                <Button target='_blank' href="https://docs.oracle.com/cd/E88353_01/html/E37839/nslookup-1.html" size="small">Learn More</Button>
            </CardActions>
        </Card>
    );
}
