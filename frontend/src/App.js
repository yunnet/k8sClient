import React, {Component} from 'react';
import Deployments from "./components/deployments";
import NamespaceInputForm from "./components/namespaceInputForm";
import DeploymentInputForm from "./components/deploymentInputForm";

class App extends Component {
    state = {
        deployments: []
    };

    updateDeployments = (deployments) => {
        this.setState({deployments: deployments});
    };

    render() {
        return (
            <div className="container">
                <div className="row mb-3">
                    <div className="col-md-3">
                        <div className="my-3 p-3 bg-white rounded shadow-sm">
                            <DeploymentInputForm/>
                        </div>
                    </div>
                    <div className="col-md-9">
                        <div className="my-3 p-3 bg-white rounded shadow-sm">
                            <NamespaceInputForm onSubmit={this.updateDeployments}/>
                        </div>
                        <p/>
                        <div className="my-3 p-3 bg-white rounded shadow-sm">
                            <Deployments deployments={this.state.deployments}/>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default App;
