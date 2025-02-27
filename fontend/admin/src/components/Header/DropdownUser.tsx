'use client'

import { useState } from 'react';
import Link from 'next/link';

const DropdownUser = () => {
    const [dropdownOpen, setDropdownOpen] = useState(false);

    return (
        <Link
            onClick={() => setDropdownOpen(!dropdownOpen)}
            className="flex items-center gap-4"
            href="#"
        >
            <span className="hidden text-right lg:block">
                <span className="block text-sm font-medium text-black dark:text-white">
                    Thomas Anree
                </span>
                <span className="block text-xs">UX Designer</span>
            </span>

            <span className="h-12 w-12 rounded-full">
                <img src="/images/user/user-01.png" alt="User" />
            </span>

           
        </Link>

    );
};

export default DropdownUser;
