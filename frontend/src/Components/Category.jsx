import React, { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { BASE_API_URL } from "../config/api.js";
import axios from "axios";
import Task from "./Task.jsx";
import { FaEdit, FaTrashAlt } from "react-icons/fa";
import TaskEditModal from "./TaskEditModal.jsx";

const Category = ({ category, categories, handleEditCategory, handleDeleteCategory }) => {
    const [isOpen, setIsOpen] = useState(false);
    const [tasks, setTasks] = useState([]);
    const [editDialogOpen, setEditDialogOpen] = useState(false);
    const [taskToEdit, setTaskToEdit] = useState(null);

    const fetchTasks = async () => {
        try {
            const response = await axios.get(
                `${BASE_API_URL}/api/tasks/category/${category.id}`,
                {
                    headers: {
                        "Content-Type": "application/json",
                    },
                    withCredentials: true,
                }
            );

            if (response.status === 200) {
                setTasks(response.data);
            }
        } catch (error) {
            toast.error("Error fetching tasks:", error);
        }
    };

    const handleDeleteTask = async (taskId) => {
        try {
            const response = await axios.delete(`${BASE_API_URL}/api/tasks/${taskId}`, {
                headers: {
                    "Content-Type": "application/json",
                },
                withCredentials: true,
            });

            if (response.status === 204) {
                toast.success("Task deleted successfully");
                setTasks((prevTasks) => prevTasks.filter((task) => task.id !== taskId));
            }
        } catch (error) {
            toast.error("Error deleting task");
        }
    };

    const handleEditTask = (task) => {
        setTaskToEdit(task);
        setEditDialogOpen(true);
    };

    const handleEditTaskSave = async (updatedTask) => {
        try {
            console.log(updatedTask);
            const response = await axios.put(
                `${BASE_API_URL}/api/tasks/${updatedTask.id}`,
                updatedTask,
                {
                    headers: {
                        "Content-Type": "application/json",
                    },
                    withCredentials: true,
                }
            );

            if (response.status === 200) {
                toast.success("Task updated successfully");

                setTasks((prevTasks) =>
                    prevTasks
                        .filter((task) => task.id !== updatedTask.id)
                        .concat(updatedTask)
                );
                window.location.reload();
                setEditDialogOpen(false);
                setTaskToEdit(null);
            }
        } catch (error) {
            toast.error("Error updating task");
        }
    };

    useEffect(() => {
        fetchTasks();
    }, [category]);

    return (
        <div className="mb-4 border border-gray-300 rounded-lg bg-white">
            <div
                className="flex items-center justify-between p-4 cursor-pointer group"
                onClick={() => setIsOpen(!isOpen)}
            >
                <div className="flex items-center">
                    <span className={`mr-2 transform ${isOpen ? "rotate-90" : ""}`}>&#9654;</span>
                    <h2 className="text-lg font-medium">{category.name}</h2>
                </div>

                <div className="opacity-0 group-hover:opacity-100 transition-opacity duration-300">
                    <button
                        onClick={(e) => {
                            e.stopPropagation();
                            handleEditCategory(category.id);
                        }}
                        className="text-blue-500 ml-8"
                    >
                        <FaEdit size={16} />
                    </button>
                    <button
                        onClick={(e) => {
                            e.stopPropagation();
                            handleDeleteCategory(category.id);
                        }}
                        className="text-red-500 ml-4"
                    >
                        <FaTrashAlt size={16} />
                    </button>
                </div>
            </div>

            {isOpen && (
                <div className="pl-8 pr-4 pb-4">
                    {tasks.length > 0 ? (
                        tasks.map((task) => (
                            <Task
                                key={task.id}
                                task={task}
                                handleEditTask={() => handleEditTask(task)}
                                handleDeleteTask={handleDeleteTask}
                            />
                        ))
                    ) : (
                        <p>No tasks available.</p>
                    )}
                </div>
            )}

            <TaskEditModal
                task={taskToEdit}
                isOpen={editDialogOpen}
                categories={categories}
                onClose={() => setEditDialogOpen(false)}
                onSave={handleEditTaskSave}
            />
        </div>
    );
};

export default Category;
