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
    const [validated, setValidated] = useState(false);

    useEffect(() => {
        const validateAndSetToken = async () => {
            const accessToken = localStorage.getItem('accessToken');
            if (accessToken) {
                const isValid = await validateToken(accessToken);
                setValidated(isValid);
            } else {
                window.location.href = "/login";
                console.log("No accessToken found");
            }
        };
        validateAndSetToken();
    }, []);

    useEffect(() => {
        if (validated) {
            const accessToken = localStorage.getItem('accessToken');
            axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
            fetchGlobalNotifications();
            fetchUserDetails();
        }
    }, [validated]);

    const validateToken = async (accessToken) => {
        try {
            const validationResponse = await fetchWithToken("/opensquare/auth/validate_token", accessToken);
            console.log(validationResponse)
            setValidated(true);
        } catch (error) {
            console.log(error);
        }
    };

    const fetchWithToken = async (url, token, options = {}) => {

        if (!token) {
            window.location.href = "/login";
        }

        const response = await axios.get(url, {
            ...options,
            headers: {
                ...options.headers,
                'Authorization': `Bearer ${token}`
            }
        })

        if (response.status === 401) {
            console.log(response);
            token = await refreshToken();
            if (token) {
                return axios.get(url, {
                    ...options,
                    headers: {
                        ...options.headers,
                        'Authorization': `Bearer ${token}`
                    }
                });
            } else {
                return response;
            }
        }
    }

    const refreshToken = async () => {
        try {
            const refreshToken = localStorage.getItem('refreshToken');
            const response = await axios.post("/opensquare/auth/refresh-token", {
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ refreshToken })
            });

            if (response.ok) {
                const data = await response.json();
                localStorage.setItem('accessToken', data.accessToken);
                localStorage.setItem('refreshToken', data.refreshToken);
                return data.accessToken;
            } else {
                throw new Error("Unable to refresh token");
            }
        } catch (error) {
            console.error("Token refresh failed", error);
            window.location.href = "/login";
        }
    }

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
