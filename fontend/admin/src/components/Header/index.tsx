import Link from 'next/link';
import React from 'react'
import DropdownUser from './DropdownUser';

export default function Header() {
    return (
        <header className="sticky top-0 z-999 flex w-full bg-white drop-shadow-1 dark:bg-boxdark dark:drop-shadow-none">
            <div className="flex flex-grow items-center justify-between px-4 py-4 shadow-2 md:px-6 2xl:px-11">
                <div className="flex items-center gap-2 sm:gap-4 lg:hidden">
                    <button
                        aria-controls="sidebar"
                        className="z-99999 block rounded-sm border border-stroke bg-white p-1.5 shadow-sm dark:border-strokedark dark:bg-boxdark lg:hidden"
                    >
                        <span className="relative block h-5.5 w-5.5 cursor-pointer">
                            <span className="du-block absolute right-0 h-full w-full">
                                <span
                                    className={`relative left-0 top-0 my-1 block h-0.5 w-0 rounded-sm bg-black delay-[0] duration-200 ease-in-out 
                                        dark:bg-white`}
                                ></span>
                                <span
                                    className={`relative left-0 top-0 my-1 block h-0.5 w-0 rounded-sm bg-black delay-150 duration-200 ease-in-out dark:bg-white`}
                                ></span>
                                <span
                                    className={`relative left-0 top-0 my-1 block h-0.5 w-0 rounded-sm bg-black delay-200 duration-200 ease-in-out dark:bg-white`}
                                ></span>
                            </span>
                            <span className="absolute right-0 h-full w-full rotate-45">
                                <span
                                    className={`absolute left-2.5 top-0 block h-full w-0.5 rounded-sm bg-black delay-300 duration-200 ease-in-out dark:bg-white`}
                                ></span>
                                <span
                                    className={`delay-400 absolute left-0 top-2.5 block h-0.5 w-full rounded-sm bg-black duration-200 ease-in-out dark:bg-white`}
                                ></span>
                            </span>
                        </span>
                    </button>
                    <Link className="block flex-shrink-0 lg:hidden" href="/">
                        <img src="/images/logo/logo.svg" alt="Logo" />
                    </Link>
                </div>

                <div className="hidden sm:block">
                    <form action="https://formbold.com/s/unique_form_id" method="POST">
                        <div className="relative">
                            <button className="absolute left-0 top-1/2 -translate-y-1/2">
                                <svg
                                    className="hidden fill-current sm:block"
                                    width="12"
                                    height="8"
                                    viewBox="0 0 12 8"
                                    fill="none"
                                    xmlns="http://www.w3.org/2000/svg"
                                >
                                    <path
                                        fillRule="evenodd"
                                        clipRule="evenodd"
                                        d="M0.410765 0.910734C0.736202 0.585297 1.26384 0.585297 1.58928 0.910734L6.00002 5.32148L10.4108 0.910734C10.7362 0.585297 11.2638 0.585297 11.5893 0.910734C11.9147 1.23617 11.9147 1.76381 11.5893 2.08924L6.58928 7.08924C6.26384 7.41468 5.7362 7.41468 5.41077 7.08924L0.410765 2.08924C0.0853277 1.76381 0.0853277 1.23617 0.410765 0.910734Z"
                                        fill=""
                                    />
                                </svg>
                            </button>

                            <input
                                type="text"
                                placeholder="Type to search..."
                                className="w-full bg-transparent pl-9 pr-4 text-black focus:outline-none xl:w-125"
                            />
                        </div>
                    </form>
                </div>


                <div className="flex items-center gap-3 2xsm:gap-7">
                    <DropdownUser />
                </div>
            </div>
        </header>
    )
}
