// TechLab - Desarrollador: Pablo De Lillo. Interfaz conectada a la API REST.
const $ = id => document.getElementById(id);
const escapeHtml = v => String(v ?? '').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const dinero = n => Number(n).toLocaleString('es-AR',{style:'currency',currency:'ARS'});
let productos=[],categorias=[],usuarios=[];
let carrito=JSON.parse(localStorage.getItem('techlab_carrito')||'{}');
let vista='catalogo';
function notificar(m,error=false){const el=$('notificacion');el.textContent=m;el.className=error?'error':'';window.scrollTo({top:0,behavior:'smooth'});}
async function api(path,options={}){
 const response=await fetch('/api'+path,{...options,headers:{'Content-Type':'application/json',...(options.headers||{})}});
 if(!response.ok){let dato={};try{dato=await response.json()}catch{}throw new Error(dato.error||`Error HTTP ${response.status}`)}
 return response.status===204?null:response.json();
}
async function ejecutar(accion){try{await accion();}catch(e){notificar(e.message,true);}}
function cambiarVista(v){vista=v;document.querySelectorAll('.view').forEach(e=>e.classList.toggle('hidden',e.id!==v));document.querySelectorAll('nav button').forEach(b=>b.classList.toggle('active',b.dataset.view===v));
 if(v==='pedidos')ejecutar(cargarHistorial);if(v==='admin')ejecutar(cargarAdmin);if(v==='carrito')dibujarCarrito();}
function guardarCarrito(){localStorage.setItem('techlab_carrito',JSON.stringify(carrito));$('cartCount').textContent=Object.values(carrito).reduce((s,n)=>s+n,0)}
async function refrescar(){[productos,categorias,usuarios]=await Promise.all([api('/productos'),api('/categorias'),api('/usuarios')]);
 $('categoriaId').innerHTML=categorias.map(c=>`<option value="${c.id}">${escapeHtml(c.nombre)}</option>`).join('');
 const actual=$('usuario').value;$('usuario').innerHTML='<option value="">Seleccionar...</option>'+usuarios.map(u=>`<option value="${u.id}">${escapeHtml(u.nombre)}</option>`).join('');$('usuario').value=actual;
 dibujarProductos();dibujarCategorias();guardarCarrito();}
function dibujarProductos(){const filtro=$('buscarProducto').value.trim().toLowerCase();const lista=productos.filter(p=>p.nombre.toLowerCase().includes(filtro)||String(p.id)===filtro);
 $('productosGrid').innerHTML=lista.map(p=>`<article class="product"><div class="image">${p.imagenUrl?`<img src="${escapeHtml(p.imagenUrl)}" alt="${escapeHtml(p.nombre)}" onerror="this.replaceWith(document.createTextNode('📦'))">`:'<span class="placeholder">▣</span>'}</div><div class="details"><span class="tag">${escapeHtml(p.categoria)}</span><h3>${escapeHtml(p.nombre)}</h3><p>${escapeHtml(p.descripcion||'Producto TechLab')}</p><div class="price">${dinero(p.precio)}</div><div class="stock">${p.stock} unidades disponibles</div><button class="add" data-id="${p.id}" ${p.stock===0?'disabled':''}>${p.stock===0?'Sin stock':'Agregar al carrito'}</button></div></article>`).join('')||'<div class="empty">No hay productos para mostrar. Usá “Gestionar productos” para crear el primero.</div>';
 $('tablaProductos').innerHTML=productos.map(p=>`<tr><td>${p.id}</td><td>${escapeHtml(p.nombre)}</td><td>${dinero(p.precio)}</td><td>${p.stock}</td><td><button class="small edit" data-id="${p.id}">Editar</button> <button class="small danger delete" data-id="${p.id}">Eliminar</button></td></tr>`).join('');}
function dibujarCategorias(){$('listaCategorias').innerHTML=categorias.map(c=>`<div class="categoryRow"><strong>${escapeHtml(c.nombre)}</strong><div><button class="small catEdit" data-id="${c.id}">Editar</button> <button class="small danger catDelete" data-id="${c.id}">Eliminar</button></div></div>`).join('');}
function agregarCarrito(id){const p=productos.find(x=>x.id===id);if(!p)return;if((carrito[id]||0)>=p.stock){notificar('No hay más stock disponible',true);return;}carrito[id]=(carrito[id]||0)+1;guardarCarrito();notificar('Producto agregado al carrito');}
function dibujarCarrito(){let total=0;const filas=Object.entries(carrito).map(([id,cant])=>{const p=productos.find(x=>x.id===Number(id));if(!p)return '';total+=p.precio*cant;return `<div class="cartRow"><div><strong>${escapeHtml(p.nombre)}</strong><p class="muted">${dinero(p.precio)} · Stock: ${p.stock}</p></div><input class="cartQty" aria-label="Cantidad" type="number" min="1" max="${p.stock}" data-id="${id}" value="${cant}"><strong>${dinero(p.precio*cant)}</strong><button class="danger small remove" data-id="${id}">Quitar</button></div>`}).join('');
 $('contenidoCarrito').innerHTML=filas?filas+`<div class="categoryRow"><h2>Total: ${dinero(total)}</h2><button id="crearPedido">Crear pedido pendiente</button></div>`:'<p class="empty">El carrito está vacío.</p>';}
async function crearPedido(){const uid=Number($('usuario').value);if(!uid)throw new Error('Seleccioná o creá un usuario antes de realizar el pedido');const lineas=Object.entries(carrito).map(([id,cantidad])=>({productoId:Number(id),cantidad}));if(!lineas.length)throw new Error('El carrito está vacío');const pedido=await api('/pedidos',{method:'POST',body:JSON.stringify({usuarioId:uid,lineas})});carrito={};guardarCarrito();dibujarCarrito();notificar('Pedido #'+pedido.id+' creado en estado PENDIENTE. Confirmalo desde Pedidos para descontar stock.');cambiarVista('pedidos');}
function renderPedido(p,admin=false){const estados=p.estado==='PENDIENTE'?['CONFIRMADO','CANCELADO']:p.estado==='CONFIRMADO'?['ENVIADO','CANCELADO']:p.estado==='ENVIADO'?['ENTREGADO']:[];
 return `<div class="order"><h3>Pedido #${p.id} · ${escapeHtml(p.estado)}</h3><p>${escapeHtml(p.usuario)} · ${new Date(p.fecha).toLocaleString('es-AR')}</p><p>${p.lineas.map(l=>escapeHtml(l.nombre)+' × '+l.cantidad).join(' · ')}</p><strong>Total ${dinero(p.total)}</strong><div class="orderControls">${estados.map(e=>`<button class="small estado" data-id="${p.id}" data-estado="${e}">${e==='CONFIRMADO'?'Confirmar y descontar stock':e==='CANCELADO'?'Cancelar pedido':'Marcar '+e.toLowerCase()}</button>`).join('')}</div></div>`;}
async function cargarHistorial(){const id=$('usuario').value;$('listaPedidos').innerHTML=id?(await api('/usuarios/'+id+'/pedidos')).map(p=>renderPedido(p)).join('')||'<p class="empty">Todavía no tenés pedidos.</p>':'<p class="empty">Seleccioná un usuario.</p>';}
async function cargarAdmin(){const [todos,bajos]=await Promise.all([api('/pedidos'),api('/productos/stock-bajo')]);$('totalProductos').textContent=productos.length;$('totalUsuarios').textContent=usuarios.length;$('totalAlertas').textContent=bajos.length;
 $('alertasStock').innerHTML=bajos.map(p=>`<div class="stockRow"><strong>${escapeHtml(p.nombre)}</strong><span>Stock ${p.stock} · Mínimo ${p.stockMinimo}</span></div>`).join('')||'<p>No hay alertas de stock.</p>';
 $('pedidosAdmin').innerHTML=todos.map(p=>renderPedido(p,true)).join('')||'<p>No hay pedidos registrados.</p>';}
function limpiarProducto(){$('formProducto').reset();$('productoId').value='';$('stockMinimo').value=5;}
function limpiarCategoria(){$('categoriaEditId').value='';$('categoriaNombre').value='';}
document.querySelectorAll('nav button').forEach(b=>b.addEventListener('click',()=>cambiarVista(b.dataset.view)));
$('abrirGestion').onclick=()=>{$('gestionProductos').classList.remove('hidden');$('gestionProductos').scrollIntoView({behavior:'smooth'})};
$('buscarProducto').oninput=dibujarProductos;
$('cancelarEdicion').onclick=limpiarProducto;
$('limpiarCategoria').onclick=limpiarCategoria;
$('recargarPedidos').onclick=()=>ejecutar(cargarHistorial);
$('usuario').onchange=()=>{if(vista==='pedidos')ejecutar(cargarHistorial)};
$('crearUsuario').onclick=()=>ejecutar(async()=>{const nombre=prompt('Nombre del usuario');if(!nombre?.trim())return;const email=prompt('Email del usuario');if(!email?.trim())return;const u=await api('/usuarios',{method:'POST',body:JSON.stringify({nombre,email})});await refrescar();$('usuario').value=u.id;notificar('Usuario registrado');});
$('formProducto').onsubmit=e=>{e.preventDefault();ejecutar(async()=>{const id=$('productoId').value;const dato={nombre:$('nombre').value,descripcion:$('descripcion').value,precio:Number($('precio').value),stock:Number($('stock').value),categoriaId:Number($('categoriaId').value),imagenUrl:$('imagenUrl').value,stockMinimo:Number($('stockMinimo').value)};
 await api('/productos'+(id?'/'+id:''),{method:id?'PUT':'POST',body:JSON.stringify(dato)});limpiarProducto();await refrescar();notificar('Producto guardado correctamente');});};
$('formCategoria').onsubmit=e=>{e.preventDefault();ejecutar(async()=>{const id=$('categoriaEditId').value;await api('/categorias'+(id?'/'+id:''),{method:id?'PUT':'POST',body:JSON.stringify({nombre:$('categoriaNombre').value})});limpiarCategoria();await refrescar();notificar('Categoría guardada correctamente');});};
document.body.addEventListener('click',e=>{const b=e.target.closest('button');if(!b)return;const id=Number(b.dataset.id);
 if(b.classList.contains('add'))agregarCarrito(id);
 if(b.classList.contains('edit')){const p=productos.find(x=>x.id===id);if(!p)return;$('gestionProductos').classList.remove('hidden');['id','nombre','descripcion','precio','stock','stockMinimo','imagenUrl','categoriaId'].forEach(k=>{const el=$(k==='id'?'productoId':k);if(el)el.value=k==='categoriaId'?p.categoriaId:p[k]??''});$('gestionProductos').scrollIntoView({behavior:'smooth'});}
 if(b.classList.contains('delete'))ejecutar(async()=>{if(!confirm('¿Eliminar este producto?'))return;await api('/productos/'+id,{method:'DELETE'});await refrescar();notificar('Producto eliminado');});
 if(b.classList.contains('catEdit')){const c=categorias.find(x=>x.id===id);$('categoriaEditId').value=id;$('categoriaNombre').value=c.nombre;}
 if(b.classList.contains('catDelete'))ejecutar(async()=>{if(!confirm('¿Eliminar categoría?'))return;await api('/categorias/'+id,{method:'DELETE'});await refrescar();notificar('Categoría eliminada');});
 if(b.classList.contains('remove')){delete carrito[id];guardarCarrito();dibujarCarrito();}
 if(b.id==='crearPedido')ejecutar(crearPedido);
 if(b.classList.contains('estado'))ejecutar(async()=>{await api('/pedidos/'+id+'/estado',{method:'PATCH',body:JSON.stringify({estado:b.dataset.estado})});await refrescar();if(vista==='pedidos')await cargarHistorial();else await cargarAdmin();notificar('Estado del pedido actualizado');});
});
document.body.addEventListener('change',e=>{if(e.target.classList.contains('cartQty')){const id=e.target.dataset.id;const p=productos.find(x=>x.id===Number(id));const cantidad=Number(e.target.value);if(!Number.isInteger(cantidad)||cantidad<1||cantidad>p.stock){notificar('Cantidad inválida o stock insuficiente',true);dibujarCarrito();return;}carrito[id]=cantidad;guardarCarrito();dibujarCarrito();}});
ejecutar(refrescar);
