import React from 'react';
import Axios from 'axios';

class LoginInputForm extends React.Component {
    state = {
        username: 'admin',
        password: '123456',
        loginOk: false
    };

    handleSubmit = async (event) => {
        event.preventDefault();
        const res = await Axios({
            method: 'post',
            url: `http://localhost:8080/api/k8sclient/v1/login`,
            data: {
                username: this.state.username,
                password: this.state.password
            }
        }).then( response =>{
            //handle success
            alert("Login successful.");
            localStorage.setItem("token", response.data.jwtToken);
            console.log(response);
            this.setState({
                loginOk: true
            })
        }).catch(function (response) {
            debugger
            //handle error
            alert("Login error: Message:" + response.message);
            console.log(response);
        });

        // this.setState(
        //     {
        //         username: '',
        //         password: '',
        //         loginOk: false
        //     }
        // );
    };

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <div className="form-group">
                    <label htmlFor="username">Username</label>
                    <input type="username"
                           className="form-control"
                           id="username"
                           placeholder="Enter username"
                           value={this.state.username}
                           onChange={event => this.setState({username: event.target.value})}
                           required
                           disabled={this.state.loginOk}
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password</label>
                    <input type="password"
                           className="form-control"
                           id="password"
                           placeholder="Enter password"
                           value={this.state.password}
                           onChange={event => this.setState({password: event.target.value})}
                           required
                           disabled={this.state.loginOk}
                    />
                </div>
                <button type="submit" className="btn btn-primary">Login</button>
            </form>
        );
    }
}

export default LoginInputForm
