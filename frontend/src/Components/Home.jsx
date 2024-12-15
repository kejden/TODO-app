import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import { BASE_API_URL } from "../config/api.js";
import axios from "axios";
import Category from "./Category.jsx";
import CategoryEditModal from "./CategoryEditModal.jsx";
import AddItemModal from "./AddItemModal.jsx";
import Task from "./Task.jsx";
import TaskEditModal from "./TaskEditModal.jsx";

const Home = () => {
    const navigate = useNavigate();
    const [categories, setCategories] = useState([]);
    const [user, setUser] = useState(null);
    const [editCategoryModalOpen, setEditCategoryModalOpen] = useState(false);
    const [categoryToEdit, setCategoryToEdit] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [tasksWithoutCategory, setTasksWithoutCategory] = useState([]);
    const [taskToEdit, setTaskToEdit] = useState(null);
    const [editTaskModalOpen, setEditTaskModalOpen] = useState(false);


    const handleAdd = () => {
        setIsModalOpen(true);
    };

    const handleAuth = async (e) => {
        e.preventDefault();
        if (user) {
            try {
                const response = await axios.post(
                    `${BASE_API_URL}/auth/logout`,
                    {},
                    {
                        headers: { "Content-Type": "application/json" },
                        withCredentials: true,
                    }
                );
                if (response.status === 200) {
                    localStorage.removeItem("user");
                    setUser(null);
                    toast.success("Logout success.");
                }
            } catch (error) {
                toast.error("Error has occurred while trying to logout. Please try again.");
            }
        } else {
            navigate("/login");
        }
    };

    const handleEditCategory = (category) => {
        // console.log(category)
        setCategoryToEdit(category);
        setEditCategoryModalOpen(true);
    };

    const handleEditCategorySave = async (updatedCategory) => {
        try {
            const response = await axios.put(
                `${BASE_API_URL}/api/categories/${updatedCategory.id}`,
                updatedCategory,
                {
                    headers: { "Content-Type": "application/json" },
                    withCredentials: true,
                }
            );

            if (response.status === 200) {
                toast.success("Category updated successfully");
                setCategories(categories.map((category) =>
                    category.id === updatedCategory.id ? response.data : category
                ));
                setEditCategoryModalOpen(false);
                setCategoryToEdit(null);
            }
        } catch (error) {
            toast.error("Error updating category");
        }
    };

    const handleDeleteCategory = async (categoryId) => {
        try {
            const response = await axios.delete(`${BASE_API_URL}/api/categories/${categoryId}`, {
                headers: { "Content-Type": "application/json" },
                withCredentials: true,
            });

            if (response.status === 204) {
                toast.success("Category deleted successfully");
                setCategories(categories.filter((category) => category.id !== categoryId));
            }
        } catch (error) {
            toast.error("Error deleting category");
        }
    };

    useEffect(() => {
        const storedUser = JSON.parse(localStorage.getItem("user"));
        if (storedUser) {
            setUser(storedUser);
        }
    }, []);

    useEffect(() => {
        const fetchCategories = async () => {
            if (user) {
                try {
                    const response = await axios.get(`${BASE_API_URL}/api/categories`, {
                        headers: {
                            "Content-Type": "application/json",
                        },
                        withCredentials: true,
                    });
                    if (response.status === 200) {
                        setCategories(response.data);
                    }
                } catch (error) {
                    toast.error("Error fetching categories");
                }
            }
        };

        fetchCategories();
    }, [user]);

    const handleAddCategory = (newCategory) => {
        setCategories((prevCategories) => [...prevCategories, newCategory]);
    };

    const handleAddTask = () => {
        window.location.reload();
    };

    useEffect(() => {
        const fetchTasksWithoutCategory = async () => {
            if (user) {
                try {
                    const response = await axios.get(`${BASE_API_URL}/api/tasks/category-none`, {
                        headers: {
                            "Content-Type": "application/json",
                        },
                        withCredentials: true,
                    });
                    if (response.status === 200) {
                        setTasksWithoutCategory(response.data);
                    }
                } catch (error) {
                    toast.error("Error fetching tasks without category");
                }
            }
        };

        fetchTasksWithoutCategory();
    }, [user]);

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
        setEditTaskModalOpen(true);
    };

    const handleEditTaskSave = async (updatedTask) => {
        try {
            // console.log(updatedTask);
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

                setTasksWithoutCategory((prevTasks) =>
                    prevTasks
                        .filter((task) => task.id !== updatedTask.id)
                        .concat(updatedTask)
                );
                window.location.reload();
                setEditTaskModalOpen(false);
                setTaskToEdit(null);
            }
        } catch (error) {
            toast.error("Error updating task");
        }
    };

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
            {!user ? (
                <div className="flex flex-col items-center justify-center min-h-screen">
                    <button
                        onClick={handleAuth}
                        className="bg-blue-500 text-white text-xl py-3 px-6 rounded-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400 mb-4"
                    >
                        Log-in
                    </button>
                </div>
            ) : (
                <>
                    <button
                        onClick={handleAdd}
                        className="bg-blue-500 text-white text-xl py-3 px-6 rounded-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400 mb-4"
                    >
                        Add
                    </button>

                    <button
                        onClick={handleAuth}
                        className="absolute top-4 right-4 bg-gray-800 text-white py-2 px-4 rounded-lg hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-600"
                    >
                        Logout
                    </button>

                    <div className="p-4 bg-gray-100 min-h-screen">
                        <div>
                            {categories && categories.length > 0 ? (
                                categories.map((category) => (
                                    <Category
                                        key={category.id}
                                        category={category}
                                        categories={categories}
                                        handleDeleteCategory={handleDeleteCategory}
                                        handleEditCategory={() => {handleEditCategory(category)}}
                                    />
                                ))
                            ) : (
                                <p>No categories available.</p>
                            )}
                            <h2 className="text-xl font-semibold mt-8 mb-4">Tasks Without Category</h2>
                            {tasksWithoutCategory && tasksWithoutCategory.length > 0 ? (
                                tasksWithoutCategory.map((task) => (
                                    <Task
                                        key={task.id}
                                        task={task}
                                        handleEditTask={() => handleEditTask(task)}
                                        handleDeleteTask={handleDeleteTask}
                                    />
                                ))
                            ) : (
                                <p>No tasks without a category.</p>
                            )}
                        </div>
                    </div>
                </>
            )}
            <CategoryEditModal
                category={categoryToEdit}
                isOpen={editCategoryModalOpen}
                onClose={() => setEditCategoryModalOpen(false)}
                onSave={handleEditCategorySave}
            />
            <AddItemModal
                isOpen={isModalOpen}
                onClose={() => setIsModalOpen(false)}
                categories={categories}
                onAddCategory={handleAddCategory}
                onAddTask={handleAddTask}
            />
            <TaskEditModal
                task={taskToEdit}
                isOpen={editTaskModalOpen}
                categories={categories}
                onClose={() => setEditTaskModalOpen(false)}
                onSave={handleEditTaskSave}
            />
        </div>
    );
};

export default Home;
