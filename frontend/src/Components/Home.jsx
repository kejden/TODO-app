import React from 'react';
import PropTypes from 'prop-types';
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import { BASE_API_URL } from "../config/api.js";
import axios from "axios";

const Home = ({ isLoggedIn, setIsLoggedIn }) => {
    const navigate = useNavigate();

    const handleAdd = () => {
        alert('Dodano element!');
    };

    const handleLogout = async (e) => {
        e.preventDefault();
        if (isLoggedIn) {
            try {
                const response = await axios.post(`${BASE_API_URL}/auth/logout`, {}, {
                    headers: { 'Content-Type': 'application/json' },
                    withCredentials: true
                });
                if (response.status === 200) {
                    setIsLoggedIn(false);
                    toast.success("Wylogowano pomyślnie.");
                }
            } catch (error) {
                toast.error("Nie udało się wylogować. Spróbuj ponownie.");
            }
        } else {
            navigate('/login');
        }
    };

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
            {/* Przyciski */}
            <button
                onClick={handleAdd}
                className="bg-blue-500 text-white text-xl py-3 px-6 rounded-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400 mb-4"
            >
                Dodaj coś
            </button>

            <button
                onClick={handleLogout}
                className="absolute top-4 right-4 bg-gray-800 text-white py-2 px-4 rounded-lg hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-600"
            >
                {isLoggedIn ? 'Wyloguj się' : 'Zaloguj się'}
            </button>
        </div>
    );
};

Home.propTypes = {
    isLoggedIn: PropTypes.bool.isRequired,
    setIsLoggedIn: PropTypes.func.isRequired,
};

export default Home;