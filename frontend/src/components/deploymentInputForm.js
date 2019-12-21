import React from 'react';
import Axios from 'axios';

class DeploymentInputForm extends React.Component {
    state = {
        namespace: '',
        name: '',
        image: '',
        replicasCount: 0,
        port: 8080
    };

    handleSubmit = async (event) => {
        event.preventDefault();

        const response = Axios({
            method: 'post',
            url: `http://localhost:8080/api/k8sclient/v1/namespace/${this.state.namespace}/deployment`,
            data: {
                namespace: this.state.namespace,
                name: this.state.name,
                image: this.state.image,
                replicasCount: this.state.replicasCount,
                port: this.state.port,
            }
        })
            .then(function (response) {
                //handle success
                alert('Deploy successful. Status:' + response.status);
                console.log(response);
            })
            .catch(function (response) {
                //handle error
                alert('Deploy error: Status:' + response.statusText + ", " + response.statusText);
                console.log(response);
            });

        this.setState(
            {
                namespace: '',
                name: '',
                image: '',
                replicasCount: 0,
                port: 8080
            }
        );
    };

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <div className="form-group">
                    <label htmlFor="namespace">Namespace</label>
                    <input type="text"
                           className="form-control"
                           id="namespace"
                           placeholder="Enter namespace"
                           value={this.state.namespace}
                           onChange={event => this.setState({namespace: event.target.value})}
                           required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="name">Name</label>
                    <input type="text"
                           className="form-control"
                           id="name"
                           placeholder="Deployment name"
                           value={this.state.name}
                           onChange={event => this.setState({name: event.target.value})}
                           required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="image">Image</label>
                    <input type="text"
                           className="form-control"
                           id="image"
                           placeholder="Deployment image"
                           value={this.state.image}
                           onChange={event => this.setState({image: event.target.value})}
                           required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="replicasCount">Replicas count</label>
                    <input type="text"
                           className="form-control"
                           id="replicasCount"
                           placeholder="Replicas count"
                           value={this.state.replicasCount}
                           onChange={event => this.setState({replicasCount: event.target.value})}
                           required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="port">Port</label>
                    <input type="text"
                           className="form-control"
                           id="port"
                           placeholder="Port"
                           value={this.state.port}
                           onChange={event => this.setState({port: event.target.value})}
                           required
                    />
                </div>
                <button type="submit" className="btn btn-primary">Deploy</button>
            </form>
        );
    }
}

export default DeploymentInputForm
