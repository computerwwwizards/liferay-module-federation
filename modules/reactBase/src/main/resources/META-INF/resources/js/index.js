//@ts-check
import { createInstance  }  from '@module-federation/enhanced/runtime';
import ReactDOM from 'react-dom/client'
import { name } from '../../../../../../package.json'
import React, { lazy, Suspense } from 'react';
ReactDOM
/**
 * 
 * @param {string} DOMId 
 * @param {string} mfUrl 
 */
export default function(DOMId, mfUrl){
	const mf = createInstance({
		name,
		remotes: [
			{
				name: 'some-mf',
				entry: mfUrl,
				version: '1'
			}
		]
	})

	const Provider = lazy(async ()=> {
		const module = await mf.loadRemote('some-mf')
		await new Promise((res)=>{
			setTimeout(res, 2000)
		})
		console.log(module)
		return module
	})

	
	const root = document.getElementById(DOMId)

	if(!root)
		throw new Error('React contianer not found');

	ReactDOM.createRoot(root).render(<React.StrictMode>
		<div>
		<Suspense fallback={'loading'}>
			<Provider />
		</Suspense>
	</div>
		</React.StrictMode>)
	console.log(1)
	console.log(DOMId, mfUrl)
}