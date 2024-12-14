import React, {useState} from 'react';
import {FaEdit, FaTrashAlt} from "react-icons/fa";
import TaskModal from './TaskModal';

const Task = ({ task, handleEditTask, handleDeleteTask  }) => {
    const [isModalOpen, setIsModalOpen] = useState(false);

    const openModal = () => {
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setIsModalOpen(false);
    };

    return (
        <div className="flex items-center justify-between p-2 mb-2 border-b border-gray-200 group">
            <div onClick={openModal} className="cursor-pointer">
                <p className="text-lg font-medium">{task.title}</p>
            </div>

            <div className="opacity-0 group-hover:opacity-100 transition-opacity duration-300">
                <button
                    onClick={() => handleEditTask(task.id)}
                    className="text-blue-500 mr-4"
                >
                    <FaEdit size={16} />
                </button>
                <button
                    onClick={() => handleDeleteTask(task.id)}
                    className="text-red-500"
                >
                    <FaTrashAlt size={16} />
                </button>
            </div>
            {isModalOpen && (
                <TaskModal task={task} onClose={closeModal} />
            )}
        </div>
    );
};

export default Task;