import * as React from 'react';
import Backdrop from '@mui/material/Backdrop';
import Box from '@mui/material/Box';
import Modal from '@mui/material/Modal';
import Fade from '@mui/material/Fade';
import Typography from '@mui/material/Typography';
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ManageAccountsIcon from "@mui/icons-material/ManageAccounts";
import ListItemText from "@mui/material/ListItemText";
import Alert from '@mui/material/Alert';
import TextField from '@mui/material/TextField';
import IconButton from "@mui/material/IconButton";
import CloseIcon from '@mui/icons-material/Close';
import Button from '@mui/material/Button';
import MenuItem from '@mui/material/MenuItem';
import SendIcon from '@mui/icons-material/Send';
import Stack from '@mui/material/Stack';
import CancelIcon from '@mui/icons-material/Cancel';
import {useEffect, useState} from "react";
import axios from "axios";
import Link from "@mui/material/Link";

function Copyright(props) {
    return (
        <Typography variant="body2" color="text.secondary" align="center" {...props}>
            {'Copyright © '}
            <Link color="inherit" target="_blank" href="https://github.com/walimorris/opensquare">
                OpenSquare
            </Link>{' '}
            {new Date().getFullYear()}
            {'.'}
        </Typography>
    );
}

const style = {
    position: 'absolute',
    borderRadius: '3%',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    height: '90vh',
    width: '60vw',
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 4,
};

const settingsModalCloseButton = {
    left: '90%'
}

const settingsModalInputField = {
    marginRight: '20%'
}

export default function SettingsModal(props) {
    const [open, setOpen] = useState(false);
    const [submitOnNoUpdates, setSubmitOnNoUpdates] = useState(false);

    // user detail values
    const [username, setUserName] = useState(props.userDetails['username']);
    const [email, setEmail] = useState(props.userDetails['email']);
    const [preferredName, setPreferredName] = useState(props.userDetails['firstName']);
    const [org, setOrg] = useState(props.userDetails['organization']);
    const [prof, setProf] = useState(props.userDetails['profession'])
    const [ageRange, setAgeRange] = useState(props.userDetails['ageRange']);

    const [organizations, setOrganizations] = useState([]);
    const [professions, setProfessions] = useState([]);
    const [ages, setAges] = useState([]);

    useEffect(() => {
        async function fetchData() {
            const token = localStorage.getItem('accessToken');
            const config = {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            };
            try {
                const organizationsResponse = await axios.get('/opensquare/api/dropdowns/organizations', config);
                setOrganizations(organizationsResponse.data);
                const professionsResponse = await axios.get('/opensquare/api/dropdowns/professions', config);
                setProfessions(professionsResponse.data);
                const agesResponse = await axios.get('/opensquare/api/dropdowns/age_ranges', config);
                setAges(agesResponse.data);
            } catch (error) {
                console.error('Error fetching dropdown options:', error);
            }
        }
        fetchData();
    }, []);

    /**
     * Setting user details initial state allows client to validate user values before
     * sending updates through API. This saves from using unnecessary writes/reads on
     * dynamo. These values are static, there is no use tracking updates because a single
     * viewing of the profile settings view will establish these initial values, and we
     * want to compare the initial values to any changes on those values in that single
     * viewing. Upon opening a new settings viewing the current values will be set and
     * those values should be the same throughout that viewing.
     *
     * @type {{ageRange, createdAt, firstName, org: unknown, userId, prof: unknown, email, username}}
     */
    const userDetails = {
        username: props.userDetails['username'],
        email: props.userDetails['email'],
        firstName: props.userDetails['firstName'],
        org: props.userDetails['organization'],
        prof: props.userDetails['profession'],
        ageRange: props.userDetails['ageRange']
    };

    // handle modal open/close
    const handleOpen = () => setOpen(true);
    const handleClose = () => {
        setSubmitOnNoUpdates(false);
        setOpen(false);
    }

    /**
     * Any updated values should be set to its original value in
     * the case of cancel closures on the profile settings view.
     */
    const handleCancelClose = () => {
        setUserName(userDetails.username);
        setEmail(userDetails.email);
        setPreferredName(userDetails.firstName);
        setOrg(userDetails.org);
        setProf(userDetails.prof);
        setAgeRange(userDetails.ageRange);
        handleClose();
    }

    // handle user detail property updates
    const handleEmailChange = (e) => setEmail(e.target.value);
    const handlePreferredNameChange = (e) => setPreferredName(e.target.value);
    const handleAgeRangeChange = (e) => setAgeRange(e.target.value);
    const handleOrgChange = (e) => setOrg(e.target.value);
    const handleProfChange = (e) => setProf(e.target.value);

    useEffect(() => {
        setEmail(props.userDetails['email'])
    }, [props]);

    useEffect(() => {
        setPreferredName(props.userDetails['firstName'])
    }, [props]);

    useEffect(() => {
        setAgeRange(props.userDetails['ageRange'])
    }, [props])

    useEffect(() => {
        setProf(props.userDetails['profession'])
    }, [props])

    useEffect(() => {
        setOrg(props.userDetails['organization'])
    }, [props])

    useEffect(() => {
        setUserName(props.userDetails['username'])
    }, [props])

    useEffect(() => {
        setSubmitOnNoUpdates(false);
    }, [])

    async function handleUpdateSubmit(e) {
        e.preventDefault();
        let userDetailsUpdateObject = createUserDetailsUpdateObject();
        console.log(JSON.stringify(userDetailsUpdateObject));
        if (Object.keys(userDetailsUpdateObject).length === 2) {
            console.log('no values need updating!')
            setSubmitOnNoUpdates(true);
        } else {
            setSubmitOnNoUpdates(false);
            await axios.request({
                method: 'post',
                maxBodyLength: Infinity,
                url: '/opensentop/api/actions/user_details/update',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify(userDetailsUpdateObject)
            }).then((response) => {
                console.log(response.data);
            }).catch((error) => {
                console.log(error)
            });
            handleClose();
            location.reload(true);
        }
    }

    /**
     * Validates update values by checking the static userDetails object against the
     * dynamic user detail properties on the profile settings page. If there are no
     * objects that need to be updated then there is no request to the dynamodb table.
     * Otherwise, an update object is built with updated user detail values. Note: the
     * partition and sort keys always need to be added for fast write to dynamo.
     *
     * @returns {{}}
     */
    function createUserDetailsUpdateObject() {
        let userDetailsUpdateObject = {};

        // dynamodb PK and SK need to be included
        userDetailsUpdateObject.userId = userDetails.userId;
        userDetailsUpdateObject.createdAt = userDetails.createdAt;

        if (!(userDetails.firstName === preferredName)) {
            userDetailsUpdateObject.firstName = preferredName;
        }
        if (!(userDetails.ageRange === ageRange)) {
            userDetailsUpdateObject.ageRange = ageRange;
        }
        if (!(userDetails.email === email)) {
            userDetailsUpdateObject.email = email;
        }
        if (!(userDetails.org === org)) {
            // needs to be set to full name in data model
            userDetailsUpdateObject.organization = org;
        }
        if (!(userDetails.prof === prof)) {
            // needs to be set to full name in data model
            userDetailsUpdateObject.profession = prof;
        }
        return userDetailsUpdateObject;
    }

    return (
        <div>
            <ListItemButton onClick={handleOpen}>
                <ListItemIcon>
                    <ManageAccountsIcon />
                </ListItemIcon>
                <ListItemText primary="Profile Settings" />
            </ListItemButton>
            <Modal
                aria-labelledby="transition-modal-title"
                aria-describedby="transition-modal-description"
                open={open}
                onClose={handleClose}
                closeAfterTransition
                slots={{ backdrop: Backdrop }}
                slotProps={{
                    backdrop: {
                        timeout: 500,
                    },
                }}
            >
                <Fade in={open}>
                    <Box sx={style}>
                        <IconButton sx={settingsModalCloseButton} color="inherit" onClick={handleCancelClose}>
                            <CloseIcon
                                fontSize="large"
                                color="action"
                                right="50%"
                            />
                        </IconButton>
                        <Typography
                            id="transition-modal-title"
                            variant="h6"
                            component="h2"
                            sx={{ marginLeft: '10%' }}>
                            Profile Settings
                        </Typography>
                        <Box
                            component="form"
                            sx={{'& .MuiTextField-root': { m: 2, width: '100%' },}}
                            noValidate
                            autoComplete="off"
                        >
                            <div>
                                <Alert
                                    sx={{ width: '75%', right: '50%', marginLeft: '12%', marginTop: '2%', marginBottom: '2%' }}
                                    severity="info">
                                    Settings are used for your site experience and notifications!
                                </Alert>
                            </div>
                            <div>
                                <TextField
                                    fullWidth
                                    size={"small"}
                                    sx={settingsModalInputField}
                                    id="outlined-search"
                                    value={preferredName}
                                    onChange={handlePreferredNameChange}
                                    label="Preferred Name"
                                    style={{width: '75%', marginLeft: '12%'}}
                                    type="text" />
                            </div>
                            <div>
                                <TextField
                                    fullWidth
                                    size={"small"}
                                    sx={settingsModalInputField}
                                    id="outlined-search"
                                    value={email}
                                    onChange={handleEmailChange}
                                    label="Company/Personal email"
                                    style={{width: '75%', marginLeft: '12%'}}
                                    type="text" />
                            </div>
                            <div>
                                <TextField
                                    fullWidth
                                    size={"small"}
                                    sx={settingsModalInputField}
                                    value={org}
                                    onChange={handleOrgChange}
                                    id="outlined-select-currency"
                                    select
                                    label="Organization"
                                    style={{width: '75%', marginLeft: '12%'}}
                                    helperText={"Please Select your organization"}
                                >
                                    {organizations.map(option => (
                                        <MenuItem key={option} value={option}>
                                            {option}
                                        </MenuItem>
                                    ))}
                                </TextField>
                            </div>
                            <div>
                                <TextField
                                    fullWidth
                                    size={"small"}
                                    sx={settingsModalInputField}
                                    id="outlined-select-currency"
                                    value={prof}
                                    onChange={handleProfChange}
                                    select
                                    label="Profession"
                                    style={{width: '75%', marginLeft: '12%'}}
                                    helperText="Please select your profession"
                                >
                                    {professions.map(option => (
                                        <MenuItem key={option} value={option}>
                                            {option}
                                        </MenuItem>
                                    ))}
                                </TextField>
                            </div>
                            <div>
                                <TextField
                                    fullWidth
                                    size={"small"}
                                    sx={settingsModalInputField}
                                    id="outlined-select-currency"
                                    select
                                    value={ageRange}
                                    onChange={handleAgeRangeChange}
                                    label="Age Group"
                                    style={{width: '75%', marginLeft: '12%'}}
                                    helperText="Please select your age group"
                                >
                                    {ages.map(option => (
                                        <MenuItem key={option} value={option}>
                                            {option}
                                        </MenuItem>
                                    ))}
                                </TextField>
                            </div>
                            <div>
                                { submitOnNoUpdates && <Alert
                                    sx={{ width: '75%', right: '50%', marginLeft: '12%', marginTop: '1%', marginBottom: '1%' }}
                                    severity="warning">
                                    There were no updates on user details!
                                </Alert> }
                            </div>
                            <div>
                                <Stack direction="row" spacing={30} sx={{ marginTop: '3%', marginLeft: '15%'}}>
                                    <Button variant="outlined" onClick={handleCancelClose} startIcon={<CancelIcon />}>
                                        Cancel
                                    </Button>
                                    <Button onClick={handleUpdateSubmit} variant="contained" endIcon={<SendIcon />}>
                                        Update
                                    </Button>
                                </Stack>
                            </div>
                            <Copyright sx={{ pt: 4, marginTop: '4%' }} />
                        </Box>
                    </Box>
                </Fade>
            </Modal>
        </div>
    );
}
