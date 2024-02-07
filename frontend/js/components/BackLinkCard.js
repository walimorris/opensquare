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
    const link = backlink.link;
    const image = props.metaImage;

    const metaDescription =  meta !== undefined ? meta['og:description'] : undefined;
    const metaImage = meta !== undefined ? meta['og:image'] : props.metaImage;
    const metaTitle = meta !== undefined ? meta['og:title'] : undefined;

    /**
     * Checks if the image from the 'meta' property returned from the backlink object
     * is a valid image.
     *
     * Valid image sources contain https, are explicitly defined and
     * are publicly available.
     *
     * @return {boolean}
     */
    function isValidMetaImage() {
        if (metaImage === undefined || !metaImage.includes('https')) {
            return false;
        }
        return !metaImage.includes('s3') && !metaImage.includes('fbsbx');
    }

    return (
        <Card sx={{ height: '80%', width: '60%', marginTop: '2%', marginLeft: 'auto', marginRight: 'auto'}}>
            <CardMedia
                sx={{ width: '60%', height: 200, marginLeft: '20%' }}
                image={isValidMetaImage() ? metaImage : image}
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
