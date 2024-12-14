import React, { useState, useEffect } from "react";

const TaskEditModal = ({ task, categories, isOpen, onClose, onSave }) => {
    const STATUS_OPTIONS = ["NEW", "IN_PROGRESS", "COMPLETED"];
    const [localTask, setLocalTask] = useState(task);

    useEffect(() => {
        if (isOpen) {
            setLocalTask(task);
        }
    }, [task, isOpen]);

    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
            <div className="bg-white p-6 rounded-lg shadow-lg w-96 max-w-full max-h-full overflow-auto">
                <h2 className="text-lg font-semibold mb-4">Edit Task</h2>
                <form
                    onSubmit={(e) => {
                        e.preventDefault();
                        onSave(localTask);
                    }}
                >
                    <div className="mb-4">
                        <label className="block text-sm font-medium mb-1">Task Title</label>
                        <input
                            type="text"
                            value={localTask?.title || ""}
                            onChange={(e) =>
                                setLocalTask({...localTask, title: e.target.value})
                            }
                            className="w-full border border-gray-300 rounded-lg p-2"
                            required
                        />
                    </div>
                    <div className="mb-4">
                        <label className="block text-sm font-medium mb-1">Description</label>
                        <textarea
                            value={localTask?.description || ""}
                            onChange={(e) =>
                                setLocalTask({...localTask, description: e.target.value})
                            }
                            className="w-full border border-gray-300 rounded-lg p-2"
                            required
                        />
                    </div>
                    <div className="mb-4">
                        <label className="block text-sm font-medium mb-1">Status</label>
                        <select
                            value={localTask?.status || "NEW"}
                            onChange={(e) =>
                                setLocalTask({...localTask, status: e.target.value})
                            }
                            className="w-full border border-gray-300 rounded-lg p-2"
                            required
                        >
                            {STATUS_OPTIONS.map((status) => (
                                <option key={status} value={status}>
                                    {status.replace("_", " ")}
                                </option>
                            ))}
                        </select>
                    </div>
                    <div className="mb-4">
                        <label className="block text-sm font-medium mb-1">Category</label>
                        <select
                            value={localTask?.categoryId || ""}
                            onChange={(e) =>
                                setLocalTask({...localTask, categoryId: e.target.value})
                            }
                            className="w-full border border-gray-300 rounded-lg p-2"
                            required
                        >
                            <option value="">Select a Category</option>
                            {categories.map((category) => (
                                <option key={category.id} value={category.id}>
                                    {category.name}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="flex justify-end">
                        <button
                            type="button"
                            onClick={onClose}
                            className="bg-gray-300 text-black px-4 py-2 rounded-lg mr-2"
                        >
                            Cancel
                        </button>
                        <button
                            type="submit"
                            className="bg-blue-500 text-white px-4 py-2 rounded-lg"
                        >
                            Save
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default TaskEditModal;
