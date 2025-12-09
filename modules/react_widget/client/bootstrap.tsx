import ReactDom from 'react-dom/client'

export function init(id: string){
  ReactDom.createRoot(document.getElementById(id)!).render(<div>Hello</div>)
}