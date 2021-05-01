import React from 'react'
import Header from '../Interface/HeaderAuth'
import jwt_decode from "jwt-decode"
import FloristShopProfile from '../Florist-shop/Homepage/FloristShopProfile'
import AdminProfile from '../Admin/AdminProfile'

if (localStorage.getItem("Token") != null) {
    var token = localStorage.getItem("Token")
    var decoded = jwt_decode(token)
}

class Profile extends React.Component {

    render() {
        console.log("In profile render")
        if (localStorage.getItem("Token") == null) {
            window.location.href = './'
        } else {
            if (decoded.role === "USER") {
                console.log("In profile render USER")
                return (
                    <div className="profile">
                        <Header/>
                        <FloristShopProfile/>
                    </div>
                )
            } else if (decoded.role === "ADMIN") {
                console.log("In profile render ADMIN")
                return (
                    <div className="profile">
                        <Header/>
                        <AdminProfile/>
                    </div>
                )
            }
        }
    }
}

export default Profile;
