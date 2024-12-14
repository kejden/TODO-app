import React, { useState, useEffect } from 'react';

const CategoryEditModal = ({ category, isOpen, onClose, onSave }) => {
    const [name, setName] = useState('');

    useEffect(() => {
        if (category) {
            setName(category.name);
        }
    }, [category, isOpen]);

    const handleSave = () => {
        if (name.trim()) {
            onSave({ ...category, name });
        }
    };

    if (!isOpen) return null;

    return (
        <div
            className="fixed inset-0 flex items-center justify-center z-50"
            style={{ backgroundColor: 'rgba(0, 0, 0, 0.5)' }}
        >
            <div className="bg-white p-6 rounded-lg w-1/3">
                <h3 className="text-lg font-medium mb-4">Edit Category</h3>
                <div className="mb-4">
                    <label className="block text-sm font-medium mb-1">Category Name</label>
                    <input
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        className="w-full border border-gray-300 rounded-lg p-2"
                        required
                    />
                </div>
                <div className="flex justify-end">
                    <button
                        onClick={onClose}
                        className="bg-gray-300 text-black px-4 py-2 rounded-md mr-2"
                    >
                        Cancel
                    </button>
                    <button
                        onClick={handleSave}
                        className="bg-blue-500 text-white px-4 py-2 rounded-md"
                    >
                        Save
                    </button>
                </div>
            </div>
        </div>
    );
};

export default CategoryEditModal;
