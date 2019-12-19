import React from 'react'

const Deployments = ({deployments: deployments}) => {
    return (
        <div>
            <table className="table table-bordered">
                <thead className="thead-dark">
                <tr>
                    <th scope="col">DeploymentId</th>
                    <th scope="col">Name</th>
                    <th scope="col">Image</th>
                </tr>
                </thead>

                {deployments.map((deployment) => (
                    <tbody>
                    <tr>
                        <td class="text-center">{deployment.deploymentId}</td>
                        <td class="text-center">{deployment.name}</td>
                        <td class="text-center">{deployment.image}</td>
                    </tr>
                    </tbody>
                ))}
            </table>
        </div>
    )
};

export default Deployments
