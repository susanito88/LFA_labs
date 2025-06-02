from graphviz import Digraph
import random


def draw_currency_dfa():
    dfa = Digraph(format='png')
    dfa.attr(rankdir='LR')

    # States
    states = ['q0', 'q1', 'q2', 'q3', 'q4', 'q5', 'q6', 'q7', 'q8', 'q9']
    accept_states = ['q3', 'q6', 'q8', 'q9']

    for state in states:
        if state in accept_states:
            dfa.node(state, shape='doublecircle')
        else:
            dfa.node(state, shape='circle')

    # Transitions
    dfa.edge('q0', 'q1', label='$')
    dfa.edge('q0', 'q7', label='[1-9]')  # Start of cent values

    dfa.edge('q1', 'q2', label='0')
    dfa.edge('q1', 'q3', label='[1-9]')

    dfa.edge('q2', 'q4', label='.')
    dfa.edge('q3', 'q3', label='[0-9]')
    dfa.edge('q3', 'q4', label='.')

    dfa.edge('q4', 'q5', label='[0-9]')
    dfa.edge('q5', 'q6', label='[0-9]')

    dfa.edge('q7', 'q8', label='c')  # Single-digit cent value
    dfa.edge('q7', 'q9', label='[0-9]')  # Second digit for cent values
    dfa.edge('q9', 'q8', label='c')  # Ending in c

    return dfa


def generate_currency_by_states():
    state = 'q0'
    output = ""

    while state not in ['q3', 'q6', 'q8', 'q9']:
        if state == 'q0':
            if random.choice([True, False]):
                output += "$"
                state = 'q1'
            else:
                digit = str(random.randint(1, 9))
                output += digit
                state = 'q7'
        elif state == 'q1':
            if random.choice([True, False]):
                output += "0"
                state = 'q2'
            else:
                digit = str(random.randint(1, 9))
                output += digit
                state = 'q3'
        elif state == 'q2':
            output += "."
            state = 'q4'
        elif state == 'q3':
            if random.choice([True, False]):
                digit = str(random.randint(0, 9))
                output += digit
            else:
                state = 'q6'
        elif state == 'q4':
            output += str(random.randint(0, 9))
            state = 'q5'
        elif state == 'q5':
            output += str(random.randint(0, 9))
            state = 'q6'
        elif state == 'q7':
            if random.choice([True, False]):
                output += "c"
                state = 'q8'
            else:
                output += str(random.randint(0, 9))
                state = 'q9'
        elif state == 'q9':
            output += "c"
            state = 'q8'

    return output


dfa = draw_currency_dfa()
dfa.render('currency_dfa', view=True)

# Generate and print 5 random valid currency amounts
valid_outputs = []
while len(valid_outputs) < 5:
    generated = generate_currency_by_states()
    if generated.startswith("$"):
        if "." in generated:
            parts = generated.split(".")
            if len(parts[1]) == 2:
                valid_outputs.append(generated)
        else:
            valid_outputs.append(generated)
    elif generated.endswith("c") and (len(generated) == 2 or len(generated) == 3):
        valid_outputs.append(generated)

for output in valid_outputs:
    print(output)
