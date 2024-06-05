import React, { useEffect, useState } from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter, Route, Routes, useNavigate } from "react-router-dom";
import axios from "axios";
import Settings from "./js/pages/Settings";
import About from "./js/pages/About";
import Dashboard from "./js/components/Dashboard";
import "@trendmicro/react-sidenav/dist/react-sidenav.css";

const App = () => {
    const [userDetails, setUserDetails] = useState(JSON.parse(localStorage.getItem('userDetails')) || {});
    const [globalNotifications, setGlobalNotifications] = useState({});
    const [validated, setValidated] = useState('');

    useEffect(() => {
        const token = localStorage.getItem('token');
        validateToken();
        if (token && validated === 'valid') {
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            fetchGlobalNotifications();
            fetchUserDetails();
        } else {
            window.location.href = "/login";
        }
    }, []);

    const validateToken = async () => {
        try {
            const validationResponse = await axios.get("/opensquare/auth/opensquare/auth/validate_token");
            setValidated(validationResponse.data);
        } catch (error) {
            console.log(error);
        }
    };

    const fetchGlobalNotifications = async () => {
        try {
            const response = await axios.get('/opensquare/admin/api/notifications/globalAll');
            setGlobalNotifications(response.data);
        } catch (error) {
            console.error(error);
        }
    };

    const fetchUserDetails = () => {
        const userDetails = localStorage.getItem('userDetails');
        if (userDetails) {
            return JSON.parse(userDetails);
        } else {
            return {};
        }
    };

    return (
        <div id='app'>
            <BrowserRouter>
                <Routes>
                    <Route path='/' element={<Dashboard userDetails={userDetails} globalNotifications={globalNotifications} />} />
                    <Route path='/settings' element={<Settings userDetails={userDetails} />} />
                    <Route path='/about' element={<About />} />
                </Routes>
            </BrowserRouter>
        </div>
    );
};
ReactDOM.render(<App />,
    document.getElementById('root')
);
