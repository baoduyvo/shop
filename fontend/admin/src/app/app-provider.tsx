'use client'
import {
    createContext,
    useCallback,
    useContext,
    useEffect,
    useState
} from 'react'
import { Account } from './types/account'

type User = Account;

const AppContext = createContext<{
    user: User | null
    setUser: (user: User | null) => void
}>({
    user: null,
    setUser: () => { },
})


export const useAppContext = () => {
    const context = useContext(AppContext)
    return context
}


export default function AppProvider({
    children
}: {
    children: React.ReactNode
}) {
    const [user, setUserState] = useState<User | null>(() => { return null })
    const setUser = useCallback(
        (user: User | null) => {
            setUserState(user)
            localStorage.setItem('user', JSON.stringify(user))
        },
        [setUserState]
    )

    useEffect(() => {
        const _user = localStorage.getItem('user')
        setUserState(_user ? JSON.parse(_user) : null)
    }, [setUserState])

    return (
        <AppContext.Provider
            value={{
                user,
                setUser
            }}
        >
            {children}
        </AppContext.Provider>
    )
}