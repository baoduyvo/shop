import type { NextRequest } from 'next/server'
import type { NextApiResponse } from 'next'
export async function POST(request: NextRequest) {
    const body = await request.json()
    const refreshToken = body?.data?.refreshToken as string
    const expiryTime = body?.data?.expiryTime as string
    if (!refreshToken) {
        return Response.json(
            { message: 'Không nhận được session token' },
            {
                status: 400
            }
        )
    }
    const expiresDate = new Date(expiryTime).toUTCString()
    return Response.json(body, {
        status: 200,
        headers: {
            'Set-Cookie': `refreshToken=${refreshToken}; Path=/; HttpOnly; Expires=${expiresDate}; SameSite=Lax; Secure`
        }
    })
}
