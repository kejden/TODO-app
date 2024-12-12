import { useState } from "react";
import PropTypes from 'prop-types';
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import { BASE_API_URL } from "../config/api.js";
import axios from "axios";

const Login = ({ setIsLoggedIn }) => {
    const [user, setUser] = useState({
        email: "",
        password: ""
    });
    const navigate = useNavigate();

    const validatePassword = (password) => {
        if (password.length < 8) {
            toast.error("Password must be at least 8 characters long.");
            return false;
        }
        return true;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!validatePassword(user.password)) {
            return;
        }
        try {
            const response = await axios.post(`${BASE_API_URL}/auth/sign-in`, user, {
                headers: { 'Content-Type': 'application/json' },
                withCredentials: true
            });

            if (response.status === 200) {
                setIsLoggedIn(true);
                navigate("/");
                toast.success("You have logged in successfully.");
            }
        } catch (error) {
            if (error.response && error.response.data) {
                toast.error(error.response.data.error || "Failed to login. Please try again.");
            } else {
                toast.error("Failed to login. Please try again.");
            }
        }
    };

    const onChange = (event) => {
        const { name, value } = event.target;
        setUser({ ...user, [name]: value });
    };

    return (
        <>
            <div className="h-screen bg-cover bg-center flex items-center justify-center w-full">
                <div className="lex items-center justify-center">
                    <div className="w-full max-w-sm bg-gray-800 text-white p-6 rounded-lg shadow-md">
                        <h2 className="text-2xl font-bold text-center mb-4">Welcome back!</h2>
                        <form onSubmit={handleSubmit} className="space-y-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-400 mb-1">
                                    EMAIL<span className="text-red-500">*</span>
                                </label>
                                <input
                                    type="text"
                                    placeholder="Enter your email"
                                    name="email"
                                    value={user.email}
                                    onChange={onChange}
                                    className="w-full px-4 py-2 bg-gray-700 text-white border border-gray-600 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                                    required
                                />
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-400 mb-1">
                                    PASSWORD <span className="text-red-500">*</span>
                                </label>
                                <input
                                    type="password"
                                    placeholder="Enter your password"
                                    name="password"
                                    value={user.password}
                                    onChange={onChange}
                                    className="w-full px-4 py-2 bg-gray-700 text-white border border-gray-600 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                                    required
                                />
                            </div>

                            <button
                                type="submit"
                                className="w-full bg-blue-500 hover:bg-blue-600 text-white font-semibold py-2 rounded transition"
                            >
                                Log In
                            </button>
                        </form>

                        <p className="text-center text-gray-400 mt-4">
                            Need an account?{" "}
                            <a href="/register" className="text-blue-400 hover:underline">
                                Register
                            </a>
                        </p>
                    </div>
                </div>
            </div>
        </>
    );
};

Login.propTypes = {
    setIsLoggedIn: PropTypes.func.isRequired,
};

export default Login;
