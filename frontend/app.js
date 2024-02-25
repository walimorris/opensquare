import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {useEffect, useState} from "react";
import Settings from "./js/pages/Settings";
import About from "./js/pages/About";
import axios from "axios";
import Dashboard from "./js/components/Dashboard";

import "@trendmicro/react-sidenav/dist/react-sidenav.css";


const App = () => {
    const [userDetails, setUserDetails] = useState({});
    const [globalNotifications, setGlobalNotifications] = useState({});

    let detailsConfig = {
        method: 'post',
        maxBodyLength: Infinity,
        url: '/opensentop/api/actions/user_details',
        headers: {
            'Content-Type': 'application/json'
        },
    };

    let globalNotificationsConfig = {
        method: 'get',
        maxBodyLength: Infinity,
        url: '/opensquare/admin/api/notifications/globalAll',
        headers: {
            'Content-Type': 'application/json'
        },
    }

    useEffect(() => {
        async function fetchUserDetails() {
            await axios.request(detailsConfig)
                .then((response) => {
                    console.log(response.data);
                    setUserDetails(response.data)
                })
                .catch((error) => {
                    console.log(error)
                })
        }
        fetchUserDetails();
    }, []);

    useEffect(() => {
        async function fetchGlobalNotifications() {
            await axios.request(globalNotificationsConfig)
                .then((response) => {
                    console.log(response.data);
                    setGlobalNotifications(response.data)
                })
                .catch((error) => {
                    console.log(error)
                })
            }
            fetchGlobalNotifications();
        }, []);

    const navigate = e => {
        console.log(e);
        if (e === 'home') {
            window.location.href = '/';
        } else {
            window.location.href = '/' + e;
        }
    }

    return(
        <div id={'app'}>
            <BrowserRouter>
                <Routes>
                    <Route path='/' element={<Dashboard userDetails={userDetails} globalNotifications={globalNotifications}/>} />
                    <Route path='/settings' element={<Settings userDetails={userDetails}/>} />
                    <Route path='/about' element={<About />} />
                </Routes>
            </BrowserRouter>
        </div>
    )
};
ReactDOM.render(
    <App />,
    document.getElementById('root')
);
