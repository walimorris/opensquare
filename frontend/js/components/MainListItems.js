import * as React from "react";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import DashboardIcon from "@mui/icons-material/Dashboard";
import YouTubeIcon from '@mui/icons-material/YouTube';
import TwitterIcon from '@mui/icons-material/Twitter';
import ListItemText from "@mui/material/ListItemText";
import Collapse from '@mui/material/Collapse';
import List from '@mui/material/List';
import PeopleIcon from "@mui/icons-material/People";
import BarChartIcon from "@mui/icons-material/BarChart";
import LayersIcon from "@mui/icons-material/Layers";
import ExpandLess from '@mui/icons-material/ExpandLess';
import ExpandMore from '@mui/icons-material/ExpandMore';
import StarBorder from '@mui/icons-material/StarBorder';
import DevicesIcon from '@mui/icons-material/Devices';
import DevicesOtherIcon from '@mui/icons-material/DevicesOther';
import OpenInNewIcon from '@mui/icons-material/OpenInNew';

export default function MainListItems(props) {
    const [openPlatform, setOpenPlatform] = React.useState(false);
    const [openTargets, setOpenTargets] = React.useState(false);

    const handlePlatformClick = () => {
        setOpenPlatform(!openPlatform);
    };

    const handleTargetsClick = () => {
        setOpenTargets(!openTargets);
    }

    const handleTrigger = (e) => {
        const dataAttribute = e.target.textContent;
        const expandAttribute = e.target.id;
        let usableValue = dataAttribute !== '' ? dataAttribute : expandAttribute;

        console.log(usableValue);
        switch (usableValue) {
            case 'Platforms':
                handlePlatformClick();
                break;
            case 'Platforms-expandmore':
                handlePlatformClick();
                return;
            case 'Platforms-expandless':
                handlePlatformClick();
                return;
            case 'Targets':
                handleTargetsClick();
                break;
            case 'Targets-expandmore':
                handleTargetsClick();
                return;
            case 'Targets-expandless':
                handleTargetsClick();
                return;
        }
        props.handleToggleAll(usableValue);
        e.preventDefault();
    }

    return (
        <React.Fragment>
            <ListItemButton onClick={handleTrigger}>
                <ListItemIcon>
                    <DashboardIcon />
                </ListItemIcon>
                <ListItemText primary="Dashboard" />
            </ListItemButton>
            <ListItemButton onClick={handleTrigger}>
                <ListItemIcon>
                    <PeopleIcon />
                </ListItemIcon>
                <ListItemText primary="Targets" />
                {openTargets ? <ExpandLess onClick={handleTrigger} id='Targets-expandless' /> : <ExpandMore onClick={handleTrigger} id='Targets-expandmore'/>}
            </ListItemButton>
            <Collapse in={openTargets} timeout="auto" unmountOnExit>
                <List component="div" disablePadding>
                    <ListItemButton onClick={handleTrigger} sx={{ pl: 4 }}>
                        <ListItemIcon>
                            <DevicesIcon />
                        </ListItemIcon>
                        <ListItemText primary="Whois" />
                    </ListItemButton>
                    <ListItemButton onClick={handleTrigger} sx={{ pl: 4 }}>
                        <ListItemIcon>
                            <DevicesOtherIcon />
                        </ListItemIcon>
                        <ListItemText primary="NSLookup" />
                    </ListItemButton>
                    <ListItemButton onClick={handleTrigger} sx={{ pl: 4 }}>
                        <ListItemIcon>
                            <OpenInNewIcon />
                        </ListItemIcon>
                        <ListItemText primary="BackLinks" />
                    </ListItemButton>
                </List>
            </Collapse>
            <ListItemButton>
                <ListItemIcon>
                    <BarChartIcon />
                </ListItemIcon>
                <ListItemText primary="Analytics" />
            </ListItemButton>
            <ListItemButton onClick={handleTrigger}>
                <ListItemIcon>
                    <LayersIcon />
                </ListItemIcon>
                <ListItemText primary="Platforms" />
                {openPlatform ? <ExpandLess id='Platforms-expandless' /> : <ExpandMore id='Platforms-expandmore' />}
            </ListItemButton>
            <Collapse in={openPlatform} timeout="auto" unmountOnExit>
                <List component="div" disablePadding>
                    <ListItemButton onClick={handleTrigger} sx={{ pl: 4 }}>
                        <ListItemIcon>
                            <YouTubeIcon />
                        </ListItemIcon>
                        <ListItemText primary="YouTube" />
                    </ListItemButton>
                    <ListItemButton sx={{ pl: 4 }}>
                        <ListItemIcon>
                            <TwitterIcon />
                        </ListItemIcon>
                        <ListItemText primary="X" />
                    </ListItemButton>
                    <ListItemButton sx={{ pl: 4 }}>
                        <ListItemIcon>
                            <StarBorder />
                        </ListItemIcon>
                        <ListItemText primary="Vkontakte" />
                    </ListItemButton>
                </List>
            </Collapse>
        </React.Fragment>
    )
}
