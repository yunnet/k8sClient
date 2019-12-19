import React, {Component} from 'react';
import Deployments from "./components/deployments";

class App extends Component {
    state = {
        deployments: []
    }

    componentDidMount() {
        fetch('http://localhost:8080/api/k8sclient/v1/deployments')
            .then(res => res.json())
            .then((data) => {
                this.setState({deployments: data})
            })
            .catch(console.log)
    }

    render() {
        return (
            <div className="container">
                <div className="row">
                    <div className="col">
                        <Deployments transactions={this.state.deployments}/>
                    </div>
                </div>
            </div>
        );
    }
}

export default App;
