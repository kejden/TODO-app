import React from 'react';
import {FaTimes} from "react-icons/fa";

const TaskModal = ({ task, onClose }) => {
    return (
        <div className="modal-overlay fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
            <div className="modal-container bg-white rounded-lg p-6 max-w-lg w-full">
                <div className="modal-header flex justify-between items-center mb-4">
                    <h2 className="text-2xl font-bold">{task.title}</h2>
                    <button
                        className="text-red-500 font-bold text-xl"
                        onClick={onClose}
                    >
                        <FaTimes />
                    </button>
                </div>
                <div className="modal-body mb-4">
                    <div className="task-details">
                        <p><strong>Description:</strong> {task.description}</p>
                        <p><strong>Status:</strong> {task.status}</p>
                    </div>
                </div>
                <div className="modal-footer flex justify-end">
                    <button
                        className="bg-blue-500 text-white py-2 px-6 rounded-lg"
                        onClick={onClose}
                    >
                        Close
                    </button>
                </div>
            </div>
        </div>
    );
};


export default TaskModal;