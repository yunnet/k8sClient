import React, {Component} from 'react';
import Deployments from "./components/deployments";
import InputForm from "./components/inputForm";

class App extends Component {
    state = {
        deployments: []
    };

    updateDeployments = (deployments) => {
        this.setState({ deployments: deployments });
    };

    render() {
        return (
            <div className="container">
                <div className="row">
                    <InputForm onSubmit={this.updateDeployments}/>
                </div>
                <div className="row"/>
                <div className="row">
                    <div className="col">
                        <Deployments deployments={this.state.deployments}/>
                    </div>
                </div>
                <div className="row"/>
            </div>
        );
    }
}

export default App;
