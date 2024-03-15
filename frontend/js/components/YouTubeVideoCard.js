import * as React from 'react';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import {CardHeader} from "@mui/material";
import Avatar from "@mui/material/Avatar";
import IconButton from "@mui/material/IconButton";
import {red} from "@mui/material/colors";
import ExpandMore from "@mui/icons-material/ExpandMore";
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import Collapse from "@mui/material/Collapse";
import {styled} from "@mui/material/styles";

export default function YouTubeVideoCard(props) {
    const ExpandMore = styled((props) => {
        const { expand, ...other } = props;
        return <IconButton {...other} />;
    })(({ theme, expand }) => ({
        transform: !expand ? 'rotate(0deg)' : 'rotate(180deg)',
        marginLeft: 'auto',
        transition: theme.transitions.create('transform', {
            duration: theme.transitions.duration.shortest,
        }),
    }));

    const [expanded, setExpanded] = React.useState(false);
    const video = props.video;

    const handleExpandClick = () => {
        setExpanded(!expanded);
    };

    // properties
    const author = video.author;
    const description = video.description;
    const likeCount = video.likeCount;
    const publishDate = video.publishDate;
    const thumbnail = video.thumbnail;
    const title = video.title;
    const transcript = video.transcript;
    const videoUrl = video.videoUrl;
    const viewCount = video.viewCount;

    return (
        <Card sx={{ height: '80%', width: '60%', marginTop: '2%', marginLeft: 'auto', marginRight: 'auto'}}>
            <CardHeader
                avatar={ <Avatar sx={{ bgcolor: red[500] }} aria-label="recipe">YT</Avatar>}
                title={title}
                subheader={publishDate} />
            <CardMedia
                sx={{ width: '30%', height: 100, marginLeft: '35%' }}
                component={"img"}
                alt={"youtube image thumbnail"}
                image={thumbnail} />
            <CardContent>
                <Typography variant="body2" color="text.secondary">
                    {description}
                </Typography>
            </CardContent>
            <CardActions disableSpacing>
                <Button target='_blank' href={videoUrl} size="small">Navigate</Button>
                <ExpandMore
                    expand={expanded}
                    onClick={handleExpandClick}
                    aria-expanded={expanded}
                    aria-label="show more"
                >
                    <ExpandMoreIcon />
                </ExpandMore>
            </CardActions>
            <CardActions>
            </CardActions>
            <Collapse in={expanded} timeout="auto" unmountOnExit>
                <CardContent>
                    <Typography paragraph>Video Transcript:</Typography>
                    <Typography paragraph>
                        {transcript}
                    </Typography>
                </CardContent>
            </Collapse>
        </Card>
    );
}