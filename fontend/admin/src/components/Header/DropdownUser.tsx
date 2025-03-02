'use client'

import { useEffect, useState } from 'react';
import Link from 'next/link';

const DropdownUser = () => {
    const [dropdownOpen, setDropdownOpen] = useState(false);
    const [user, setUser] = useState<{ email: string; role: string } | null>(null);

    useEffect(() => {
        if (typeof window !== 'undefined') {
            const storedUser = localStorage.getItem('user');
            if (storedUser) {
                const parsedUser = JSON.parse(storedUser);
                setUser(parsedUser.user); // Assuming your 'user' object is inside the 'user' key
            }
        }
    }, []);

    return (
        <Link
            onClick={() => setDropdownOpen(!dropdownOpen)}
            className="flex items-center gap-4"
            href="#"
        >
            <span className="hidden text-right lg:block">
                <span className="block text-sm font-medium text-black dark:text-white">
                    {user ? user.email : 'Guest'}
                </span>
                <span className="block text-xs">
                    Role: {user ? user.role : 'Guest'}
                </span>
            </span>

            <span className="h-12 w-12 rounded-full">
                <img src="/images/user/user-01.png" alt="User" />
            </span>


        </Link>

    );
};

export default DropdownUser;
