<script>
	let name = 'world';
    let greeting = "-not set-";

	const handleUpdateClick = async () => {
	    greeting = (await fetch(
	        "/greeting/" + name
	    ).then((r) => r.json())).message;
	};
</script>

<input bind:value={name} placeholder="enter your name">
<p>Hello {name || 'stranger'}!</p>

<button on:click="{handleUpdateClick}">Greet via server</button>

<h1>The server says: {greeting}</h1>