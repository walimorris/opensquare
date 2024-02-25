import * as React from 'react';
import Chip from '@mui/material/Chip';
import Box from '@mui/material/Box';

export default function NotificationChip(props) {
    const notifications = props.notifications;
    return (
        <Box sx={{ width: 400 }}>
            <Chip color="primary"
                sx={{
                    position: 'absolute',
                    height: 'auto',
                    '& .MuiChip-label': {
                        display: 'block',
                        whiteSpace: 'normal',
                    },
                }}
                label={notifications[0].message} // update component with all notifications
            />
        </Box>
    );
}