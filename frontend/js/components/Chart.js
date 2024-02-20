import * as React from 'react';
import { useTheme } from '@mui/material/styles';
import Card from '@mui/material/Card';
import CardMedia from '@mui/material/CardMedia';
import { CardActionArea } from '@mui/material';
import Title from './Title';
import {ResponsiveContainer} from "recharts";

// Generate Sales Data
function createData(time, amount) {
    return { time, amount };
}

export default function Chart() {
    const theme = useTheme();

    return (
        <React.Fragment>
            <Title>Today</Title>
            <ResponsiveContainer>
                <Card>
                    <CardActionArea>
                        <CardMedia
                            component="iframe"
                            style={{maxWidth: 830, height: 480, background: "#F1F5F4", border: "none", borderRadius: "2px", boxShadow: "0 2px 10px 0 rgba(70, 76, 79, .2)"}}
                            src="https://charts.mongodb.com/charts-project-0-abbrz/embed/charts?id=65d4ea34-193b-4a49-8e71-508c0a11a970&maxDataAge=3600&theme=light&autoRefresh=true"
                            alt="Global Opensqaure Frequency of Analysis by Platform"
                        />
                    </CardActionArea>
                </Card>
            </ResponsiveContainer>
        </React.Fragment>
    );
}
