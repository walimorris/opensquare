import * as React from 'react';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';

export default function BackLinkCard(props) {
    const backlink = props.backlink;
    const meta = props.backlink.pagemap !== undefined && props.backlink.pagemap.metatags !== undefined ? props.backlink.pagemap.metatags[0] : undefined;

    const backlinkDescription = backlink.snippet;
    const backlinkTitle = backlink.title;
    const link = backlink.formattedUrl;

    const metaDescription =  meta !== undefined ? meta['og:description'] : undefined;
    const metaTitle = meta !== undefined ? meta['og:title'] : undefined;
    const DEFAULT_IMAGE = '/images/default-image-thumbnail.png';

    function renderImage() {
        if (backlink.pageMap.cse_image !== undefined) {
            const cse = backlink.pageMap.cse_image;
            const cseLength = cse.length;
            if (cseLength > 0) {
                const src = cse[0].src;
                console.log(src);
                if (!src.includes('x-raw-image')) {
                    return src;
                }
            }
        }
        return DEFAULT_IMAGE;
    }

    return (
        <Card sx={{ height: '80%', width: '60%', marginTop: '2%', marginLeft: 'auto', marginRight: 'auto'}}>
            <CardMedia
                sx={{ width: '60%', height: 200, marginLeft: '20%' }}
                image={renderImage()}
                title={metaTitle !== undefined ? metaTitle : backlinkTitle}
            />
            <CardContent>
                <Typography gutterBottom variant="h5" component="div">
                    {metaTitle !== undefined ? metaTitle : backlinkTitle}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                    {metaDescription !== undefined ? metaDescription : backlinkDescription}
                </Typography>
            </CardContent>
            <CardActions>
                <Button target='_blank' href={link} size="small">Navigate</Button>
            </CardActions>
        </Card>
    );
}
