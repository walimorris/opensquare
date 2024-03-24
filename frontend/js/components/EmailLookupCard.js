import * as React from 'react';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import StringUtil from "../utils/StringUtil";

export default function EmailLookupCard() {
    return (
        <Card sx={{ width: '60%', marginTop: '2%', marginLeft: 'auto', marginRight: 'auto'}}>
            <CardMedia
                sx={{ height: 140 }}
                image="/images/disposable-email.jpeg"
                title="emaillookup"
            />
            <CardContent>
                <Typography gutterBottom variant="h5" component="div">
                    Email Lookup
                </Typography>
                <Typography variant="body2" color="text.secondary">{StringUtil.emailLookupInfo}<b>opensentop@gmail.com</b>.
                </Typography>
            </CardContent>
            <CardActions>
                <Button target='_blank' href="https://en.wikipedia.org/wiki/Disposable_email_address" size="small">Learn More</Button>
            </CardActions>
        </Card>
    );
}