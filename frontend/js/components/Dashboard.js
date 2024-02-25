import * as React from 'react';
import { styled, createTheme, ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import MuiDrawer from '@mui/material/Drawer';
import Box from '@mui/material/Box';
import MuiAppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import List from '@mui/material/List';
import Typography from '@mui/material/Typography';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import Badge from '@mui/material/Badge';
import Container from '@mui/material/Container';
import Grid from '@mui/material/Grid';
import Paper from '@mui/material/Paper';
import Link from '@mui/material/Link';
import MenuIcon from '@mui/icons-material/Menu';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import NotificationsIcon from '@mui/icons-material/Notifications';
import Avatar from '@mui/material/Avatar';
import Chip from '@mui/material/Chip';
import Stack from '@mui/material/Stack';
import MainListItem from './MainListItems';
import SecondaryListItems from './SecondaryListItems';
import TertiaryListItems from './TertiaryListItems';
import Chart from './Chart';
import MostAnalyzedPlatform from './MostAnalyzedPlatform';
import Orders from './Orders';
import Button from "@mui/material/Button";
import LogoutIcon from '@mui/icons-material/Logout';
import Whois from "./Whois";
import Nslookup from './Nslookup';
import BackLinks from './BackLinks';
import YouTubePlatform from "./YouTubePlatform";
import EmailLookup from "./EmailLookup";
import NotificationChip from "./NotificationChip";

function Copyright(props) {
    return (
        <Typography variant="body2" color="text.secondary" align="center" {...props}>
            {'Copyright © '}
            <Link color="inherit" target="_blank" href="https://bitbucket.org/intelligence-opensent/opensentop/src/master/">
                OpenSquare
            </Link>{' '}
            {new Date().getFullYear()}
            {'.'}
        </Typography>
    );
}

const drawerWidth = 240;

const AppBar = styled(MuiAppBar, {
    shouldForwardProp: (prop) => prop !== 'open',
})(({ theme, open }) => ({
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(['width', 'margin'], {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
    }),
    ...(open && {
        marginLeft: drawerWidth,
        width: `calc(100% - ${drawerWidth}px)`,
        transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    }),
}));

const Drawer = styled(MuiDrawer, { shouldForwardProp: (prop) => prop !== 'open' })(
    ({ theme, open }) => ({
        '& .MuiDrawer-paper': {
            position: 'relative',
            whiteSpace: 'nowrap',
            width: drawerWidth,
            transition: theme.transitions.create('width', {
                easing: theme.transitions.easing.sharp,
                duration: theme.transitions.duration.enteringScreen,
            }),
            boxSizing: 'border-box',
            ...(!open && {
                overflowX: 'hidden',
                transition: theme.transitions.create('width', {
                    easing: theme.transitions.easing.sharp,
                    duration: theme.transitions.duration.leavingScreen,
                }),
                width: theme.spacing(7),
                [theme.breakpoints.up('sm')]: {
                    width: theme.spacing(9),
                },
            }),
        },
    }),
);

const defaultTheme = createTheme({
    palette: {
        white: {
            main: '#ffffff',
            dark: '#f8f8f8',
            darker: '#f5f5f5'
        }
    }}
);

export default function Dashboard(props) {
    const [open, setOpen] = React.useState(true);
    const [renderChart, setRenderChart] = React.useState(true);
    const [renderMostAnalyzed, setRenderMostAnalyzed] = React.useState(true);
    const [renderOrders, setRenderOrders] = React.useState(true);
    const [renderWhois, setRenderWhois] = React.useState(false);
    const [renderNslookup, setRenderNslookup] = React.useState(false);
    const [renderBacklinks, setRenderBacklinks] = React.useState(false);
    const [renderEmailLookup, setRenderEmailLookup] = React.useState(false);

    // platforms
    const [renderYouTubePlatform, setRenderYouTubePlatform] = React.useState(false);

    // notification chip
    const notifications = props.globalNotifications;
    const [showNotifications, setShowNotifications] = React.useState(false);

    const toggleDrawer = () => {
        setOpen(!open);
    };

    const toggleNotifications = () => {
        setShowNotifications(!showNotifications);
    }

    const handleToggleAll = (childData) => {
        console.log(childData);
        toggleAll(childData);
    }

    function toggleAll(childData) {
        if (childData === 'Dashboard' && (!isDashboardUIRendered())) {

            // dashboard items render
            setRenderChart(true);
            setRenderOrders(true);
            setRenderMostAnalyzed(true);

            // non dashboard items remove
            setRenderWhois(false);
            setRenderNslookup(false);
            setRenderBacklinks(false);
            setRenderEmailLookup(false);
            setRenderYouTubePlatform(false);
        } else if (childData === 'Whois') {
            // remove non-whois items
            setRenderChart(false);
            setRenderOrders(false);
            setRenderMostAnalyzed(false);
            setRenderNslookup(false);
            setRenderBacklinks(false);
            setRenderEmailLookup(false);
            setRenderYouTubePlatform(false);

            // render whois items
            setRenderWhois(true);
        } else if (childData === 'NSLookup') {
            // remove non-nslookup items
            setRenderChart(false);
            setRenderOrders(false);
            setRenderMostAnalyzed(false);
            setRenderWhois(false);
            setRenderBacklinks(false);
            setRenderEmailLookup(false);
            setRenderYouTubePlatform(false);


            // render nslookup items
            setRenderNslookup(true);
        } else if (childData === 'BackLinks') {
            // remove non-backlink items
            setRenderChart(false);
            setRenderOrders(false);
            setRenderMostAnalyzed(false);
            setRenderWhois(false);
            setRenderNslookup(false);
            setRenderEmailLookup(false);
            setRenderYouTubePlatform(false);


            // render backlinks items
            setRenderBacklinks(true);
        } else if (childData === 'EmailLookup') {
            // remove non-email lookup items
            setRenderChart(false);
            setRenderOrders(false);
            setRenderMostAnalyzed(false);
            setRenderWhois(false);
            setRenderNslookup(false);
            setRenderBacklinks(false);
            setRenderYouTubePlatform(false);


            // render email lookup items
            setRenderEmailLookup(true);
        } else if (childData === 'YouTube') {
            // remove non-youtube items
            setRenderChart(false);
            setRenderOrders(false);
            setRenderMostAnalyzed(false);
            setRenderWhois(false);
            setRenderNslookup(false);
            setRenderBacklinks(false);
            setRenderEmailLookup(false);

            setRenderYouTubePlatform(true);
        }
    }

    function isDashboardUIRendered() {
        return (renderChart && renderOrders && renderMostAnalyzed);
    }


    const handleLogout = () => {
        window.location.href = '/logout';
    }

    function notificationsLabel(count) {
        if (count === 0) {
            return 'no notifications';
        }
        if (count > 99) {
            return 'more than 99 notifications';
        }
        return `${count} notifications`;
    }

    return (
        <ThemeProvider theme={defaultTheme}>
            <Box sx={{ display: 'flex' }}>
                <CssBaseline />
                <AppBar position="absolute" open={open}>
                    <Toolbar
                        sx={{
                            pr: '24px', // keep right padding when drawer closed
                        }}
                    >
                        <IconButton
                            edge="start"
                            color="inherit"
                            aria-label="open drawer"
                            onClick={toggleDrawer}
                            sx={{
                                marginRight: '36px',
                                ...(open && { display: 'none' }),
                            }}
                        >
                            <MenuIcon />
                        </IconButton>
                        <Typography
                            component="h1"
                            variant="h6"
                            color="inherit"
                            noWrap
                            sx={{ flexGrow: 1 }}
                        >
                            Dashboard
                        </Typography>
                        <Stack direction="row" spacing={1} sx={{marginRight: '65%'}}>
                            <Chip
                                avatar={<Avatar alt="W" src="/images/professional_me.jpeg" />}
                                label="Avatar"
                                variant="outlined"
                            />
                        </Stack>
                        <IconButton color="inherit" sx={{ marginRight: '5%' }}>
                            <Badge badgeContent={2} color="secondary">
                                <NotificationsIcon onClick={toggleNotifications}/>
                            </Badge>
                            {showNotifications && <Badge>
                                <NotificationChip notifications={notifications}/>
                            </Badge>}
                        </IconButton>

                        <Button onClick={handleLogout} size="small" color="white" endIcon={<LogoutIcon />}>
                            Logout
                        </Button>
                    </Toolbar>
                </AppBar>
                <Drawer variant="permanent" open={open}>
                    <Toolbar
                        sx={{
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'flex-end',
                            px: [1],
                        }}
                    >
                        <IconButton onClick={toggleDrawer}>
                            <ChevronLeftIcon />
                        </IconButton>
                    </Toolbar>
                    <Divider />
                    <List component="nav">
                        <MainListItem handleToggleAll={handleToggleAll} />
                        <Divider sx={{ my: 1 }} />
                        <SecondaryListItems />
                        <Divider sx={{ my: 1 }} />
                        <TertiaryListItems userDetails={props.userDetails}/>
                    </List>
                </Drawer>
                <Box
                    component="main"
                    sx={{
                        backgroundColor: (theme) =>
                            theme.palette.mode === 'light'
                                ? theme.palette.grey[100]
                                : theme.palette.grey[900],
                        flexGrow: 1,
                        height: '100vh',
                        overflow: 'auto',
                    }}
                >
                    <Toolbar />
                    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
                        <Grid container spacing={3}>
                            {/* Chart */}
                            {renderChart &&  <Grid item xs={12} md={8} lg={9}>
                                <Paper
                                    sx={{
                                        p: 2,
                                        display: 'flex',
                                        flexDirection: 'column',
                                        height: 580,
                                    }}
                                >
                                    <Chart />
                                </Paper>
                            </Grid> }
                            {/* Recent MostAnalyzedPlatform */}
                            { renderMostAnalyzed && <Grid item xs={12} md={4} lg={3}>
                                <Paper
                                    sx={{
                                        p: 2,
                                        display: 'flex',
                                        flexDirection: 'column',
                                        height: 240,
                                    }}
                                >
                                    <MostAnalyzedPlatform userDetails={props.userDetails} />
                                </Paper>
                            </Grid> }
                            {/* Recent Orders */}
                            { renderOrders && <Grid item xs={12}>
                                <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>
                                    <Orders />
                                </Paper>
                            </Grid> }
                            {/* Whois component */}
                            { renderWhois && <Grid item xs={12}>
                                <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>
                                    <Whois />
                                </Paper>
                            </Grid> }
                            {/* nslookup component */}
                            { renderNslookup && <Grid item xs={12}>
                                <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>
                                    <Nslookup />
                                </Paper>
                            </Grid> }
                            {/* backlinks component */}
                            { renderBacklinks && <Grid item xs={12}>
                                <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>
                                    <BackLinks />
                                </Paper>
                            </Grid> }
                            {/* email lookup component */}
                            { renderEmailLookup && <Grid item xs={12}>
                                <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>
                                    <EmailLookup />
                                </Paper>
                            </Grid> }
                            {/* youtube component */}
                            { renderYouTubePlatform && <Grid item xs={12}>
                                <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>
                                    <YouTubePlatform isSelected={true} />
                                </Paper>
                            </Grid> }
                        </Grid>
                        <Copyright sx={{ pt: 4 }} />
                    </Container>
                </Box>
            </Box>
        </ThemeProvider>
    );
}
