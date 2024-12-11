import {useState} from "react";
import {useNavigate} from "react-router-dom";
import toast from "react-hot-toast";
import {BASE_API_URL} from "../config/api.js";
import axios from "axios";


const Register = () => {
    const [user, setUser] = useState({
        email: "",
        password: "",
    });
    // const dispatch = useDispatch();
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
        if(!validatePassword(user.password)) {
            return;
        }
        try {
            const response = await axios.post(`${BASE_API_URL}/auth/sign-up`, user, {
                headers: {'Content-Type': 'application/json'},
            });

            if (response.status === 201) {
                navigate("/login");
                toast.success("You have registered successfully.");
            }
        } catch (error) {
            if (error.response && error.response.data) {
                toast.error(error.response.data.error || "Failed to register. Please try again.");
            } else {
                toast.error("Failed to register. Please try again.");
            }
        }
    };


    const onChange = (event) =>{
        const {name, value} = event.target;
        setUser({...user, [name]: value});
    }


    return (
        <>
            <div className="h-screen bg-cover bg-center flex items-center justify-center w-full">
                <div className="flex items-center justify-center">
                    <div className="w-full max-w-sm bg-gray-800 text-white p-6 rounded-lg shadow-md">
                        <h2 className="text-2xl font-bold text-center mb-4">Sign Up!</h2>
                        <form onSubmit={handleSubmit} className="space-y-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-400 mb-1">
                                    EMAIL
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
                                    PASSWORD
                                </label>
                                <input
                                    type="password"
                                    name="password"
                                    value={user.password}
                                    onChange={onChange}
                                    placeholder="Enter your password"
                                    className="w-full px-4 py-2 bg-gray-700 text-white border border-gray-600 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                                    required
                                />
                            </div>
                            <button
                                type="submit"
                                className="w-full bg-blue-500 hover:bg-blue-600 text-white font-semibold py-2 rounded transition"
                            >
                                Register
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </>
    );
};

export default Register;