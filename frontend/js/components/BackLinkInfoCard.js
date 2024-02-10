import * as React from 'react';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';

export default function BackLinkInfoCard() {
    return (
        <Card sx={{ width: '60%', marginTop: '2%', marginLeft: 'auto', marginRight: 'auto'}}>
            <CardMedia
                sx={{ height: 140 }}
                image="/images/backlink-chain.png"
                title="backlinks"
            />
            <CardContent>
                <Typography gutterBottom variant="h5" component="div">
                    Backlink Chain
                </Typography>
                <Typography variant="body2" color="text.secondary">
                    OpenSquare digital targeting tools provides this backlink tool that gives you
                    the ability to query a url and expose its backlinks. A backlink is a link from
                    some other website (the referrer) to that web resource (the referent). A web
                    resource may be (for example) a website, web page, or web directory. Opensquare
                    uses <a target='_blank' href="https://support.google.com/webmasters/answer/9049606?hl=en">Google's</a> powerful indexing capabilities to provide this service.
                    <br></br>
                    <br></br>
                    <b>Tips for Inquiry:</b><br></br>
                    <b>1.</b><i>What sites are being back-linked from disposable email domains?</i><br></br>
                    <b>2.</b><i>What sites are being back-linked from known malicious resources that you should be aware of?</i><br></br>
                    <b>3.</b><i>How do backlink results effect the credibility of certain sites?</i>
                </Typography>
            </CardContent>
            <CardActions>
                <Button target='_blank' href="https://en.wikipedia.org/wiki/Backlink" size="small">Learn More</Button>
            </CardActions>
        </Card>
    );
}