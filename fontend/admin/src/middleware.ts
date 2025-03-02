import { NextResponse } from 'next/server'
import type { NextRequest } from 'next/server'

const privatePaths = ['/users', '/categories', '/products', '/logout']

export function middleware(request: NextRequest) {
    const { pathname } = request.nextUrl
    const sessionToken = request.cookies.get('refreshToken')?.value

    if (privatePaths.some((path) => pathname.startsWith(path)) && !sessionToken) {
        return NextResponse.redirect(new URL('/login', request.url))
    }
    if (sessionToken && pathname === '/login') {
        return NextResponse.redirect(new URL('/', request.url));
    }

    return NextResponse.next()
}

export const config = {
    matcher: ['/login', '/users', '/categories', '/products', '/logout']
}