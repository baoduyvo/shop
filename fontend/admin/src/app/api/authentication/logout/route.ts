import type { NextRequest } from 'next/server'
import type { NextApiResponse } from 'next'
import { cookies } from 'next/headers'
import { Account } from '@/app/types/account'

type ResponseData = {
    message: string
}

export async function POST(
    request: NextRequest
) {
    const cookieStore = await cookies()
    const theme = cookieStore.get('refreshToken')
    const body = await request.json()

    if (body) {
        const parsedUser: Account = JSON.parse(body);
        console.log('Access Token:', parsedUser.access_token);
    } else {
        console.log('No user found in localStorage');
    }
    // logoutFromNextServerToServer(body?.access_token)
    return Response.json("Logout SuccessFully", {
        status: 200
    })
}


async function logoutFromNextServerToServer(accessToken: string) {

    try {
        await fetch(process.env.NEXT_PUBLIC_HOST_GATEWAY + 'jwt/auth/logout',
            {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                },
            }).then((data) => data.json());


    } catch (error) {
        console.log('error - ' + error);
    }
}

function logoutFromNextClientToNextServer() {

}

function slideSessionFromNextServerToServer() {

}