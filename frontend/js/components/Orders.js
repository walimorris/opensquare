import * as React from 'react';
import Link from '@mui/material/Link';
import Typography from '@mui/material/Typography';
import Title from './Title';

function preventDefault(event) {
    event.preventDefault();
}

function getCurrentDate() {
    const date = new Date();
    let day = date.getDate();
    let month = getMonthString(date.getMonth());
    let year = date.getFullYear();
    return `as of ${day} ${month}, ${year}`;
}

function getMonthString(monthInt) {
    switch (monthInt) {
        case 0:
            return 'January';
        case 1:
            return 'February';
        case 2:
            return 'March';
        case 3:
            return 'April';
        case 4:
            return 'May';
        case 5:
            return 'June';
        case 6:
            return 'July';
        case 7:
            return 'August';
        case 8:
            return 'September';
        case 9:
            return 'October';
        case 10:
            return 'November';
        default:
            return 'December';
    }
}

export default function Deposits() {
    return (
        <React.Fragment>
            <Title>Recent Platform Runs</Title>
            <Typography component="p" variant="h4">
                --------
            </Typography>
            <Typography color="text.secondary" sx={{ flex: 1 }}>
                as of {getCurrentDate()}
            </Typography>
            <div>
                <Link color="primary" href="#" onClick={preventDefault}>
                    View More
                </Link>
            </div>
        </React.Fragment>
    );
}
