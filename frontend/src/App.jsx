import './App.css'
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import {Toaster} from "react-hot-toast";
import Login from "./Components/Login.jsx";
import Register from "./Components/Register.jsx";

function App() {

  return (
    <>
        <Router>
            <Toaster
                position="top-center"
                reverseOrder={false}
            />
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
            </Routes>
        </Router>
    </>
  )
}

export default App
