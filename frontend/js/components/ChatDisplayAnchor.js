import * as React from 'react';
import Box from "@mui/material/Box";
import ChatDisplay from "./ChatDisplay";
import Button from "@mui/material/Button";
import {Drawer} from "@mui/material";

export default function ChatDisplayAnchor() {
    const [state, setState] = React.useState({ right: false });

    const toggleDrawer = (anchor, open) => (event) => {
        if (event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
            return;
        }
        setState({ ...state, [anchor]: open });
    };

    const chat = (anchor) => (
        <Box role="presentation">
            <ChatDisplay />
        </Box>
    );

    return (
        <div>
            <React.Fragment key={'right'}>
                <Button onClick={toggleDrawer('right', true)}>Chat</Button>
                <Drawer
                    anchor='right'
                    open={state['right']}
                    onClose={toggleDrawer('right', false)}
                >
                    {chat('right')}
                </Drawer>
            </React.Fragment>
        </div>
    )
}