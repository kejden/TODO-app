import React, { useState } from "react";
import toast from "react-hot-toast";
import { BASE_API_URL } from "../config/api.js";
import axios from "axios";
import {FaTimes} from "react-icons/fa";

const AddItemModal = ({
                          isOpen,
                          onClose,
                          categories,
                          onAddCategory,
                          onAddTask,
                      }) => {
    const [isCategoryForm, setIsCategoryForm] = useState(true);
    const [newCategory, setNewCategory] = useState("");
    const [newTask, setNewTask] = useState({
        title: "",
        description: "",
        categoryId: null,
        status: "NEW",
    });

    const handleAddCategory = async () => {
        if (!newCategory) {
            toast.error("Category name is required");
            return;
        }

        try {
            const response = await axios.post(
                `${BASE_API_URL}/api/categories`,
                { name: newCategory },
                {
                    headers: {
                        "Content-Type": "application/json",
                    },
                    withCredentials: true,
                }
            );
            if (response.status === 201) {
                toast.success("Category added successfully");
                onAddCategory(response.data);
                setNewCategory("");
                onClose();
            }
        } catch (error) {
            toast.error(error.response.data.error);
        }
    };

    const handleAddTask = async () => {
        if (!newTask.title || !newTask.description) {
            toast.error("Both Title and Description are required");
            return;
        }

        try {
            console.log(newTask);
            const response = await axios.post(
                `${BASE_API_URL}/api/tasks`,
                newTask,
                {
                    headers: {
                        "Content-Type": "application/json",
                    },
                    withCredentials: true,
                }
            );
            if (response.status === 201) {
                toast.success("Task added successfully");
                onAddTask();
                setNewTask({
                    name: "",
                    description: "",
                    categoryId: null,
                    status: "NEW",
                });
                onClose();
            }
        } catch (error) {
            toast.error("Error adding task");
        }
    };

    return (
        isOpen && (
            <div className="fixed inset-0 bg-gray-500 bg-opacity-50 flex justify-center items-center z-50">
                <div className="bg-white p-8 rounded-lg shadow-lg w-96">
                    <div className="flex justify-between items-center mb-4">
                        <div className="flex space-x-4">
                            <button
                                className={`px-4 py-2 ${isCategoryForm ? "bg-blue-500" : "bg-gray-300"} text-white rounded-md`}
                                onClick={() => setIsCategoryForm(true)}
                            >
                                Category
                            </button>
                            <button
                                className={`px-4 py-2 ${!isCategoryForm ? "bg-blue-500" : "bg-gray-300"} text-white rounded-md`}
                                onClick={() => setIsCategoryForm(false)}
                            >
                                Task
                            </button>
                        </div>
                        <button
                            className="text-gray-500 hover:text-black font-bold text-xl"
                            onClick={onClose}
                        >
                            <FaTimes />
                        </button>
                    </div>

                    {isCategoryForm ? (
                        <div>
                            <h3 className="text-xl font-semibold mb-4">Add Category</h3>
                            <input
                                type="text"
                                value={newCategory}
                                onChange={(e) => setNewCategory(e.target.value)}
                                placeholder="Category name"
                                className="w-full p-2 border border-gray-300 rounded-md mb-4"
                            />
                            <button
                                onClick={handleAddCategory}
                                className="w-full bg-blue-500 text-white px-4 py-2 rounded-md"
                            >
                                Add Category
                            </button>
                        </div>
                    ) : (
                        <div>
                            <h3 className="text-xl font-semibold mb-4">Add Task</h3>
                            <input
                                type="text"
                                value={newTask.title}
                                onChange={(e) =>
                                    setNewTask({ ...newTask, title: e.target.value })
                                }
                                placeholder="Task title"
                                className="w-full p-2 border border-gray-300 rounded-md mb-4"
                            />
                            <textarea
                                value={newTask.description}
                                onChange={(e) =>
                                    setNewTask({ ...newTask, description: e.target.value })
                                }
                                placeholder="Task description"
                                className="w-full p-2 border border-gray-300 rounded-md mb-4"
                            />
                            <select
                                value={newTask.categoryId}
                                onChange={(e) =>
                                    setNewTask({ ...newTask, categoryId: e.target.value })
                                }
                                className="w-full p-2 border border-gray-300 rounded-md mb-4"
                            >
                                <option value="">Select Category</option>
                                {categories.map((category) => (
                                    <option key={category.id} value={category.id}>
                                        {category.name}
                                    </option>
                                ))}
                            </select>
                            <select
                                value={newTask.status}
                                onChange={(e) =>
                                    setNewTask({ ...newTask, status: e.target.value })
                                }
                                className="w-full p-2 border border-gray-300 rounded-md mb-4"
                            >
                                <option value="NEW">NEW</option>
                                <option value="IN_PROGRESS">IN_PROGRESS</option>
                                <option value="COMPLETED">COMPLETED</option>
                            </select>
                            <button
                                onClick={handleAddTask}
                                className="w-full bg-blue-500 text-white px-4 py-2 rounded-md"
                            >
                                Add Task
                            </button>
                        </div>
                    )}
                </div>
            </div>
        )
    );
};

export default AddItemModal;
